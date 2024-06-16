package com.datnguyen.rem.entity.embedded;

import com.datnguyen.rem.entity.Order;
import com.datnguyen.rem.entity.Product;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review_ID {
   @ManyToOne
   @JoinColumn(name = "product_id")
   Product product;
   @ManyToOne
   @JoinColumn(name = "order_id")
   Order order;
}
