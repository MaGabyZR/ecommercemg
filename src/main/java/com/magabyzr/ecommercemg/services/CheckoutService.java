package com.magabyzr.ecommercemg.services;

import com.magabyzr.ecommercemg.dtos.CheckoutRequest;
import com.magabyzr.ecommercemg.dtos.CheckoutResponse;
import com.magabyzr.ecommercemg.entities.Order;
import com.magabyzr.ecommercemg.exceptions.CartEmptyException;
import com.magabyzr.ecommercemg.exceptions.CartNotFoundException;
import com.magabyzr.ecommercemg.repositories.CartRepository;
import com.magabyzr.ecommercemg.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@RequiredArgsConstructor                                                    //Only fields declared as final will be initialized.
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Transactional                                                                          //To make all an ATOMIC operation.
    public CheckoutResponse checkout(CheckoutRequest request) throws StripeException {
        //find the cart in the repository.
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        //a. if it does not exist, return 400 bad request.
        if(cart == null){
            throw new CartNotFoundException();
        }
        //b.if cart is empty
        if (cart.isEmpty()){
            throw new CartEmptyException();
        }
        var order = Order.fromCart(cart, authService.getCurrentUser());

        //Save it to our repository.
        orderRepository.save(order);

        //Handle stripe exceptions.
        try{
            //Create a checkout session.
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel?orderId=" + order.getId());
            //add all order items to the builder and for each item create a Stripe line item.
            order.getItems().forEach(item -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("USD")
                                        .setUnitAmountDecimal(
                                                item.getUnitPrice()
                                                    .multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build()
                                        )
                                        .build()
                        ).build();
                builder.addLineItem(lineItem);
            });
            //Build a session.
            var session = Session.create(builder.build());

            //Clear the cart.
            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getUrl());
        }
        catch (StripeException ex){
            System.out.println(ex.getMessage());
            orderRepository.delete(order);
            throw ex;
        }
    }
}
