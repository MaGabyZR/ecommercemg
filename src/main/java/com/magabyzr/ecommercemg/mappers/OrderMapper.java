package com.magabyzr.ecommercemg.mappers;

import com.magabyzr.ecommercemg.dtos.OrderDto;
import com.magabyzr.ecommercemg.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
