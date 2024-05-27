package com.datnguyen.rem.repository.critea;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@AllArgsConstructor
public class SearchCriteria {
    String key; // name field
    String operation; // =
    Object value;
}
