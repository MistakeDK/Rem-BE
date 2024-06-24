package com.datnguyen.rem.dto.request;

import com.datnguyen.rem.enums.PaymentType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NonNull
    String userId;
    @NonNull
    String address;
    @NonNull
    String name;
    @NonNull
    String phone;
    String promotionCode;
    @NonNull
    PaymentType paymentType;
}
