package com.magabyzr.ecommercemg.carts;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(){
        super("Cart not found");
    }
}
