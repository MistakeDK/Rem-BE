package com.datnguyen.rem.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NonNull
    String oldPassword;
    @Size(min=8,message = "PASSWORD_INVALID")
    String newPassword;
}
