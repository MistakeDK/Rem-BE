package com.datnguyen.rem.dto.request;

import com.datnguyen.rem.enums.ActionCartQuantity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetailRequest {
    @NonNull
    String productId;
    @NonNull
    String action;
    @NonNull
    Integer quantity;
}
