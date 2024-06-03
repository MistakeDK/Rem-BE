package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.PromotionRequest;
import com.datnguyen.rem.dto.response.PromotionResponse;
import com.datnguyen.rem.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    Promotion toPromotion(PromotionRequest request);
    PromotionResponse toPromotionResponse(Promotion promotion);
}
