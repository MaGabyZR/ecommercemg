package com.magabyzr.ecommercemg.orders;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Order not found!");
    }
}
