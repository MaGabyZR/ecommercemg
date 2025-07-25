package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.dtos.CartDto;
import com.magabyzr.ecommercemg.entities.Cart;
import com.magabyzr.ecommercemg.mappers.CartMapper;
import com.magabyzr.ecommercemg.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

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
}
