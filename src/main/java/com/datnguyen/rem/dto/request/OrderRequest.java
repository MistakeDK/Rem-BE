package com.datnguyen.rem.dto.request;

import com.datnguyen.rem.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    String userId;
    String address;
    String name;
    String phone;
    String promotionCode;
    PaymentType paymentType;
}
