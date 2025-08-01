package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.AddItemToCartRequest;
import com.magabyzr.ecommercemg.dtos.CartDto;
import com.magabyzr.ecommercemg.dtos.CartItemDto;
import com.magabyzr.ecommercemg.dtos.UpdateCartItemRequest;
import com.magabyzr.ecommercemg.exceptions.CartNotFoundException;
import com.magabyzr.ecommercemg.exceptions.ProductNotFoundException;
import com.magabyzr.ecommercemg.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        //call CartService to generate a new cart.
        var cartDto = cartService.createCart();
        //generate a URI
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        //Option 2 with uri
        return ResponseEntity.created(uri).body(cartDto);

        //Option1 without uri
        //return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }
    //Add items to a cart
    @PostMapping(("/{cartId}/items"))
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request){
        //call CartService to add to the cart
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
    //GET a cart
    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        //call CartService to get a cart and return it.
        return cartService.getCart(cartId);
    }
    //Update a cart item.
    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        //call CartService to update an item and return it.
        return cartService.updateItem(cartId, productId, request.getQuantity());
    }
    //Remove an item from a cart.
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(                                        //? to be able to return a Map.
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        //call CartService to remove item.
        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }
    //Clear a cart, or deleting its items.
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {
        //call CartService to clear cart.
        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }
    //handle exceptions.
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
        );
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Product was not found in the cart.")
        );
    }
}
