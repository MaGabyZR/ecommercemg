package com.magabyzr.ecommercemg.services;

import com.magabyzr.ecommercemg.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession (Order order);
}
