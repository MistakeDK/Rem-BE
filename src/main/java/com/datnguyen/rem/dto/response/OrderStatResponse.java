package com.datnguyen.rem.dto.response;

import com.datnguyen.rem.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderStatResponse {
    OrderStatus status;
    Long count;
}
