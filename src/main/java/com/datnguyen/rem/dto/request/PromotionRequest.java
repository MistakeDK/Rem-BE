package com.datnguyen.rem.dto.request;

import com.datnguyen.rem.enums.PromotionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRequest {
    @NonNull
    String promotionCode;
    @NonNull
    PromotionType type;
    @NonNull
    Double value;
}
