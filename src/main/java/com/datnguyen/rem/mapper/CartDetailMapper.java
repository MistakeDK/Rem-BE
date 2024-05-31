package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.CartDetailRequest;
import com.datnguyen.rem.dto.response.CartDetailResponse;
import com.datnguyen.rem.dto.response.CategoryResponse;
import com.datnguyen.rem.entity.CartDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartDetailMapper {
    @Mapping(source = "cartDetailId.product.id", target = "id")
    @Mapping(source = "cartDetailId.product.name", target = "name")
    @Mapping(source = "cartDetailId.product.price", target = "price")
    @Mapping(source = "cartDetailId.product.img", target = "img")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source ="cartDetailId.product.active" ,target ="active" )
    CartDetailResponse toCartDetailResponse(CartDetail cartDetail);
}
