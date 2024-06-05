package com.datnguyen.rem.dto.request;

import com.datnguyen.rem.enums.PromotionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRequest {
    @NotEmpty(message = "PROMOTION_CODE_IS_EMPTY")
    String promotionCode;
    @NotEmpty(message = "PROMOTION_TYPE_IS_EMPTY")
    String type;
    Double value;

}
