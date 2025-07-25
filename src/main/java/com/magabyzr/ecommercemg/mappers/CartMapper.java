package com.magabyzr.ecommercemg.mappers;

import com.magabyzr.ecommercemg.dtos.CartDto;
import com.magabyzr.ecommercemg.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
