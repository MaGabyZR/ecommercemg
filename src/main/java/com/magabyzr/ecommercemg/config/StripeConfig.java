package com.magabyzr.ecommercemg.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//Configuration class to inject out stripe key and initialize Stripe.
@Configuration
public class StripeConfig {
    //1. declare the stripe variable
    @Value("${stripe.secretKey}")
    private String secretKey;
    //2. define a method for initializing Stripe.
    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }

}
