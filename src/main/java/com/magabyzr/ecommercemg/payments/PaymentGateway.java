package com.magabyzr.ecommercemg.payments;

import com.magabyzr.ecommercemg.orders.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession (Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
