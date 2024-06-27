package com.datnguyen.rem.entity;

import com.datnguyen.rem.entity.eventListener.PromotionListener;
import com.datnguyen.rem.enums.PromotionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(PromotionListener.class)
public class Promotion {
    @Id
    String promotionCode;
    @Enumerated(EnumType.STRING)
    PromotionType type;
    Double value;
    @Builder.Default
    Boolean active=true;
}
