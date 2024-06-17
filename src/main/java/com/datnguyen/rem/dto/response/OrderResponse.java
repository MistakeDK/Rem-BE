package com.datnguyen.rem.dto.response;

import com.datnguyen.rem.entity.OrderDetail;
import com.datnguyen.rem.enums.OrderStatus;
import com.datnguyen.rem.enums.PaymentType;
import com.datnguyen.rem.enums.PromotionType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    Date time_Create;
    String address;
    String name;
    String phone;
    OrderStatus status;
    Double valueVoucher;
    PromotionType promotionType;
    Boolean isPaid;
    PaymentType paymentType;
    List<OrderDetailResponse> orderDetails;
}
