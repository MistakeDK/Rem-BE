package com.datnguyen.rem.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DobValidatior.class})
public @interface DobConstraint {
    String message() default "date of birth must have 18 year old";
    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
