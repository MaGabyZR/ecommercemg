package com.magabyzr.ecommercemg.carts;

import com.magabyzr.ecommercemg.common.ErrorDto;
import com.magabyzr.ecommercemg.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
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
    @Operation(summary = "Adds a product to a cart.")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The ID of a cart.")
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
    public ResponseEntity<?> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Cart not found.")
        );
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto("Product was not found in the cart.")
        );
    }
}
