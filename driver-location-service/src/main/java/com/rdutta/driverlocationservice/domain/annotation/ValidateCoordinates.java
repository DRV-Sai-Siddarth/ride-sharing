package com.rdutta.driverlocationservice.domain.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CoordinatesValidator.class)
public @interface ValidateCoordinates {
    String message() default "Invalid latitude or longitude range";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
