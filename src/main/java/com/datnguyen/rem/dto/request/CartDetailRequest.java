package com.datnguyen.rem.dto.request;

import com.datnguyen.rem.enums.ActionCartQuantity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetailRequest {
    String productId;
    String action;
}
