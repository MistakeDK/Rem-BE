package com.datnguyen.rem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    Set<Product> products;
}
