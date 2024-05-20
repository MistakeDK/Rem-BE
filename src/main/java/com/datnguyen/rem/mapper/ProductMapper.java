package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductRequest productRequest);
}
