package com.datnguyen.rem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    String productId;
    String productName;
    Double price;
    Integer quantity;
    String img;
    Boolean isReview;

}
