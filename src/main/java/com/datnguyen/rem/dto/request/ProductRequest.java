package com.datnguyen.rem.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @Size(min = 4,message = "NAME_INVALID")
    String name;
    @Min(value = 1,message = "PRICE_INVALID")
    Double price;
    @NotEmpty(message = "IMG_IS_EMPTY")
    String img;
    @NotEmpty(message = "DESCRIPTION_IS_EMPTY")
    String description;
    @NotEmpty()
    String categoryId;
}
