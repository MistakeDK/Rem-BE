package com.datnguyen.rem.entity;

import com.datnguyen.rem.enums.OrderStatus;
import com.datnguyen.rem.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`")
public class Order extends AbstractEntity {
    @Id
    String id;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    OrderStatus status=OrderStatus.RECEIVED;
    @Enumerated(EnumType.STRING)
    PaymentType paymentType;
    String address;
    String name;
    String phone;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<OrderDetail> orderDetails;
    @ManyToOne
    @JoinColumn(name = "promotion_code",nullable = true)
    Promotion promotion;
    @Builder.Default
    Boolean isPaid=false;
    public Date getTimeCreate(){
        return super.getTimeCreate();
    }
}
