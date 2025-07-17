package com.magabyzr.ecommercemg.mappers;

import com.magabyzr.ecommercemg.dtos.ProductDto;
import com.magabyzr.ecommercemg.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
}
