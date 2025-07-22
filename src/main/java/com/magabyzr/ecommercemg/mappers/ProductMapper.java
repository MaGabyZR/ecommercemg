package com.magabyzr.ecommercemg.mappers;

import com.magabyzr.ecommercemg.dtos.ProductDto;
import com.magabyzr.ecommercemg.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category_id", source = "category.id")
    ProductDto toDto(Product product);

    //method to return a product object.
    Product toEntity(ProductDto productDto);
}
