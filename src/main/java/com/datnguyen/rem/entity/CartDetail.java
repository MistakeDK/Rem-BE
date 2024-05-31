package com.datnguyen.rem.entity;

import com.datnguyen.rem.entity.embedded.CartDetail_ID;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CartDetail {
    @EmbeddedId
    CartDetail_ID cartDetailId;
    Integer quantity;
}
