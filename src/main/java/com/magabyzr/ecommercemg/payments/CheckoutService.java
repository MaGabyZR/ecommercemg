package com.magabyzr.ecommercemg.payments;

import com.magabyzr.ecommercemg.entities.Order;
import com.magabyzr.ecommercemg.carts.CartEmptyException;
import com.magabyzr.ecommercemg.carts.CartNotFoundException;
import com.magabyzr.ecommercemg.carts.CartRepository;
import com.magabyzr.ecommercemg.repositories.OrderRepository;
import com.magabyzr.ecommercemg.auth.AuthService;
import com.magabyzr.ecommercemg.carts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor                                                    //Only fields declared as final will be initialized.
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional                                                                          //To make all an ATOMIC operation.
    public CheckoutResponse checkout(CheckoutRequest request){
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

        //Handle stripe exceptions, call del PaymentGateway and pass the order.
        try{
            var session = paymentGateway.createCheckoutSession(order);

            //Clear the cart.
            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        }
        catch (PaymentException ex){
            orderRepository.delete(order);
            throw ex;
        }
    }
    //Handle Webhook event and payment status.
    public void handleWebhookEvent(WebhookRequest request){
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }
}
