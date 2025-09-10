package com.magabyzr.ecommercemg.payments;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
