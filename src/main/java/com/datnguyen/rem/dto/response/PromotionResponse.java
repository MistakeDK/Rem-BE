package com.datnguyen.rem.dto.response;

import com.datnguyen.rem.enums.PromotionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionResponse {
    PromotionType type;
    Double value;
    Boolean active;
}
