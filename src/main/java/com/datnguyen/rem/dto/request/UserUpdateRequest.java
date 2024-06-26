package com.datnguyen.rem.dto.request;

import com.datnguyen.rem.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min=8,message = "PASSWORD_INVALID")
    String password;
    @DobConstraint(min = 18,message = "DOB_INVALID")
    LocalDate dob;
    String email;
    String role;
}
