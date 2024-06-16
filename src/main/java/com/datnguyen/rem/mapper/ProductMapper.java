package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.dto.response.ProductDetailResponse;
import com.datnguyen.rem.dto.response.ProductResponse;
import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.repository.ReviewRepository;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductRequest productRequest);
    ProductResponse toProductResponse(Product product);
    ProductDetailResponse toProductDetailResponse(Product product,@Context ReviewRepository repository);
    @AfterMapping
    default void calculate(Product product,@MappingTarget ProductDetailResponse response
            ,@Context ReviewRepository repository){
        response.setTotalReview(repository.countById_Product_Id(product.getId()));
        response.setRateStar(repository.calculateAVG(product.getId()));
    }
}
