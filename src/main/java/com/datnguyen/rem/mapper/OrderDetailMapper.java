package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.response.OrderDetailResponse;
import com.datnguyen.rem.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(source = "product.id",target = "productId")
    @Mapping(source = "product.img",target = "img")
    @Mapping(source = "product.name",target = "productName")
    @Mapping(source = "quantity",target = "quantity")
    @Mapping(source = "price",target = "price")
    @Mapping(source = "isReview",target = "isReview")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
}
