package com.datnguyen.rem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    String id;
    String name;
    Double price;
    String img;
    String description;
    Boolean isActive;
    Long totalReview;
    Double rateStar;
}
