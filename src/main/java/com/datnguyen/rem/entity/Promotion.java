package com.datnguyen.rem.entity;

import com.datnguyen.rem.enums.PromotionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    String promotionCode;
    @Enumerated(EnumType.STRING)
    PromotionType type;
    Double value;
    @Builder.Default
    Boolean active=true;
}
