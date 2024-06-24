package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.dto.request.ProductUpdateRequest;
import com.datnguyen.rem.dto.response.ProductDetailResponse;
import com.datnguyen.rem.dto.response.ProductResponse;
import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.repository.CategoryRepository;
import com.datnguyen.rem.repository.ReviewRepository;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductRequest productRequest);
    ProductResponse toProductResponse(Product product);

    @Mapping(source = "category.name",target = "category")
    @Mapping(source = "category.id",target = "categoryId")
    ProductDetailResponse toProductDetailResponse(Product product,@Context ReviewRepository repository);
    @AfterMapping
    default void calculate(Product product,@MappingTarget ProductDetailResponse response
            ,@Context ReviewRepository repository){
        response.setTotalReview(repository.countById_Product_Id(product.getId()));
        response.setRateStar(repository.calculateAVG(product.getId()));
    }


    @Mapping(target = "id",ignore = true)
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request,@Context CategoryRepository repository);
    @AfterMapping
    default void setCategory(@MappingTarget Product target,
                             ProductUpdateRequest request,
                             @Context CategoryRepository repository){
        var category= repository.findById(request.getCategoryId()).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        target.setCategory(category);
    }
}
