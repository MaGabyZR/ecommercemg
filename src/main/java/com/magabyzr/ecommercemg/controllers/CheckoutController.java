package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.CheckoutRequest;
import com.magabyzr.ecommercemg.dtos.CheckoutResponse;
import com.magabyzr.ecommercemg.dtos.ErrorDto;
import com.magabyzr.ecommercemg.entities.Order;
import com.magabyzr.ecommercemg.repositories.CartRepository;
import com.magabyzr.ecommercemg.repositories.OrderRepository;
import com.magabyzr.ecommercemg.services.AuthService;
import com.magabyzr.ecommercemg.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request
    ){
        //find the cart in the repository.
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        //a. if it does not exist, return 400 bad request.
        if(cart == null){
            return ResponseEntity.badRequest().body(
                    new ErrorDto("Cart not found.")
            );
        }
        //b.if cart is empty
        if (cart.getItems().isEmpty()){
            return ResponseEntity.badRequest().body(
                    new ErrorDto("Cart is empty.")
            );
        }
        var order = Order.fromCart(cart, authService.getCurrentUser());

        //Save it to our repository.
        orderRepository.save(order);
        //Clear the cart.
        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
