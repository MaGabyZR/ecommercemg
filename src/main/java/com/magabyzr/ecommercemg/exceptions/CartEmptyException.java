package com.magabyzr.ecommercemg.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(){
        super("Cart is empty.");
    }
}
