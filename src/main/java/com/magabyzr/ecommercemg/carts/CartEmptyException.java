package com.magabyzr.ecommercemg.carts;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(){
        super("Cart is empty.");
    }
}
