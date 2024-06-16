package com.datnguyen.rem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    @NonNull
    String productId;
    @NonNull
    String orderId;
    @NonNull
    Integer rateStar;
    @NonNull
    String review;
}
