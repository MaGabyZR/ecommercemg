package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.AddItemToCartRequest;
import com.magabyzr.ecommercemg.dtos.CartDto;
import com.magabyzr.ecommercemg.dtos.CartItemDto;
import com.magabyzr.ecommercemg.dtos.UpdateCartItemRequest;
import com.magabyzr.ecommercemg.entities.Cart;
import com.magabyzr.ecommercemg.mappers.CartMapper;
import com.magabyzr.ecommercemg.repositories.CartRepository;
import com.magabyzr.ecommercemg.repositories.ProductRepository;
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
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);
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
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);

        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
    //GET a cart
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }
    //Update a cart item.
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(                                        //? to be able to return a Map in the error status.
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        //find a cart in the repository.
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found.")
            );
        }
        //to find a product in the cart, call getItem from Cart.java.
        var cartItem = cart.getItem(productId);
        //if it does not exist return a not found error.
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Product was not found in the cart.")
            );
        }
            //if successful update the quantity and save the cart.
            cartItem.setQuantity(request.getQuantity());
            cartRepository.save(cart);

            return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }
}
