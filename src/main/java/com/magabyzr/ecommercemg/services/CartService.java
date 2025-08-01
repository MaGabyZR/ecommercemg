package com.magabyzr.ecommercemg.services;

import com.magabyzr.ecommercemg.dtos.CartDto;
import com.magabyzr.ecommercemg.dtos.CartItemDto;
import com.magabyzr.ecommercemg.entities.Cart;
import com.magabyzr.ecommercemg.exceptions.CartNotFoundException;
import com.magabyzr.ecommercemg.exceptions.ProductNotFoundException;
import com.magabyzr.ecommercemg.mappers.CartMapper;
import com.magabyzr.ecommercemg.repositories.CartRepository;
import com.magabyzr.ecommercemg.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    //create a new cart
    public CartDto createCart(){
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }
    //add to cart.
    public CartItemDto addToCart(UUID cartId, Long productId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }
    //get a cart
    public CartDto getCart(UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cartMapper.toDto(cart);
    }
    //update and Item.
    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity){
        //find a cart in the repository.
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        //find a cart item.
        var cartItem = cart.getItem(productId);
        //if it does not exist return a not found error.
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }
        //if successful update the quantity and save the cart.
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }
    //Remove and item from the cart.
    public void removeItem(UUID cartId, Long productId){
        //validate the cart
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.removeItem(productId);

        cartRepository.save(cart);
    }
    //Clear the items of a cart
    public void clearCart(UUID cartId){
        //validate the cart
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.clear();

        cartRepository.save(cart);

    }
}
