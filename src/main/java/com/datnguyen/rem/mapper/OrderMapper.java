package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.OrderRequest;
import com.datnguyen.rem.dto.response.OrderResponse;
import com.datnguyen.rem.entity.CartDetail;
import com.datnguyen.rem.entity.Order;

import com.datnguyen.rem.entity.OrderDetail;
import com.datnguyen.rem.entity.Promotion;
import com.datnguyen.rem.repository.PromotionRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring",uses = {OrderDetailMapper.class, PromotionRepository.class})
public interface OrderMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "promotionCode",target = "promotion.promotionCode")
    Order toOrder(OrderRequest request);



    @Mapping(source = "cartDetailId.product", target = "product")
    @Mapping(source = "cartDetailId.product.price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    OrderDetail toOrderDetail(CartDetail cartDetail);
    Set<OrderDetail> toListOrderDetail(List<CartDetail> cartDetail);

    @Mapping(target = "valueVoucher",source = "promotion.value")
    @Mapping(source = "promotion.type",target = "promotionType")
    @Mapping(source = "orderDetails",target = "orderDetails")
    @Mapping(source = "timeCreate",target = "time_Create")
    @Mapping(source = "paymentType",target = "paymentType")
    @Mapping(source = "id",target = "id")
    OrderResponse toOrderResponse(Order order);


}
