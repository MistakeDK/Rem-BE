package com.datnguyen.rem.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CategoryRequest {
    @NonNull
    String name;
}
