package com.datnguyen.rem.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {
    @NonNull
    String categoryId;
    @NonNull
    String description;
    String id;
    @NonNull
    String img;
    @NonNull
    Boolean isActive;
    @NonNull
    String name;
    @NonNull
    Double price;
}
