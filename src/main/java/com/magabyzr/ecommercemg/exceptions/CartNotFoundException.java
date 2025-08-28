package com.magabyzr.ecommercemg.exceptions;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(){
        super("Cart not found");
    }
}
