package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.CheckoutRequest;
import com.magabyzr.ecommercemg.dtos.CheckoutResponse;
import com.magabyzr.ecommercemg.entities.Order;
import com.magabyzr.ecommercemg.entities.OrderItem;
import com.magabyzr.ecommercemg.entities.OrderStatus;
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

import java.util.Map;

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
                    Map.of("error", "Cart not found.")
            );
        }
        //b.if cart is empty
        if (cart.getItems().isEmpty()){
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Cart is empty.")
            );
        }
        //c.create an order, save it, clear the cart and return 200 ok
        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        //convert each cart item to and order item.
        cart.getItems().forEach(item -> {
            //1. create an order item
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            //2. add it to our order.
            order.getItems().add(orderItem);
        });
        //Save it to our repository.
        orderRepository.save(order);
        //Clear the cart.
        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
