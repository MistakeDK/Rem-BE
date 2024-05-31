package com.datnguyen.rem.entity.embedded;

import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.entity.User;
import jakarta.persistence.Embeddable;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Optional;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDetail_ID {
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

}
