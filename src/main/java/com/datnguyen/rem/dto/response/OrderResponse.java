package com.datnguyen.rem.dto.response;

import com.datnguyen.rem.entity.OrderDetail;
import com.datnguyen.rem.enums.OrderStatus;
import com.datnguyen.rem.enums.PromotionType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Date time_Create;
    String address;
    String name;
    String phone;
    OrderStatus status;
    Double valueVoucher;
    PromotionType promotionType;
    List<OrderDetailResponse> orderDetails;
}
