package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.CheckoutRequest;
import com.magabyzr.ecommercemg.dtos.CheckoutResponse;
import com.magabyzr.ecommercemg.dtos.ErrorDto;
import com.magabyzr.ecommercemg.exceptions.CartEmptyException;
import com.magabyzr.ecommercemg.exceptions.CartNotFoundException;
import com.magabyzr.ecommercemg.exceptions.PaymentException;
import com.magabyzr.ecommercemg.services.CheckoutService;
import com.magabyzr.ecommercemg.services.WebhookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request){
        return checkoutService.checkout(request);
    }
    //Webhook endpoint
    @PostMapping("/webhook")
    public void handleWebhook(
            //verify the request came from Stripe and it was a legit request.
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ){
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }
    //Handle payment exceptions, in case we cannot create a Checkout Session.
    @ExceptionHandler({PaymentException.class})
    public ResponseEntity<?> handlePaymentException(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }
    //Handle the exceptions and return the right response to the client.
    @ExceptionHandler({CartEmptyException.class, CartNotFoundException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
