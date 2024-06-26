package com.datnguyen.rem.entity;

import com.datnguyen.rem.entity.eventListener.ProductListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ProductListener.class)
public class Product extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    Double price;
    String img;
    @Builder.Default
    Boolean isActive=true;
    String description;
    @Builder.Default
    Boolean isHot=false;
    @Builder.Default
    Boolean isNew=false;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    Category category;
}
