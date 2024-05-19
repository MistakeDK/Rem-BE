package com.datnguyen.rem.entity;

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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String price;
    String img;
    @Builder.Default
    boolean isActive=true;
    String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
