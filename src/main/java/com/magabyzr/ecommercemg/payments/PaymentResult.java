package com.magabyzr.ecommercemg.payments;

import com.magabyzr.ecommercemg.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
