package com.magabyzr.ecommercemg.mappers;

import com.magabyzr.ecommercemg.dtos.CartDto;
import com.magabyzr.ecommercemg.dtos.CartItemDto;
import com.magabyzr.ecommercemg.entities.Cart;
import com.magabyzr.ecommercemg.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
