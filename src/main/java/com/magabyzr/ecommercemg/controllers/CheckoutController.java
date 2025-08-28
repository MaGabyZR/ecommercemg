package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.CheckoutRequest;
import com.magabyzr.ecommercemg.dtos.CheckoutResponse;
import com.magabyzr.ecommercemg.dtos.ErrorDto;
import com.magabyzr.ecommercemg.exceptions.CartEmptyException;
import com.magabyzr.ecommercemg.exceptions.CartNotFoundException;
import com.magabyzr.ecommercemg.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request){
        return checkoutService.checkout(request);
    }
    //Handle the exceptions and return the right response to the client.
    @ExceptionHandler({CartEmptyException.class, CartNotFoundException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
