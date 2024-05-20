package com.datnguyen.rem.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @Size(min = 4,message = "NAME_INVALID")
    String name;
    @Min(value = 1000,message = "PRICE_INVALID")
    Double price;
    String img;
    String description;
    String categoryId;
}
