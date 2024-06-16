package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.ReviewRequest;
import com.datnguyen.rem.dto.response.ReviewResponse;
import com.datnguyen.rem.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "productId",target = "id.product.id")
    @Mapping(source = "orderId",target = "id.order.id")
    Review toReview(ReviewRequest request);
    @Mapping(source = "id.order.user.username",target = "username")
    ReviewResponse toReviewResponse(Review review);
}
