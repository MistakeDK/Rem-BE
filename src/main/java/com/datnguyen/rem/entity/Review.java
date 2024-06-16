package com.datnguyen.rem.entity;

import com.datnguyen.rem.entity.embedded.Review_ID;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Review extends AbstractEntity{
    @EmbeddedId
    Review_ID id;
    Integer rateStar;
    String review;

}
