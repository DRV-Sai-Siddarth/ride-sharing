package com.rdutta.driverlocationservice.domain.annotation;

import com.rdutta.driverlocationservice.domain.dto.Coordinates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CoordinatesValidator implements ConstraintValidator<ValidateCoordinates, Coordinates> {
    @Override
    public boolean isValid(Coordinates coordinates, ConstraintValidatorContext context) {

        if (coordinates == null) {
            return true;
        }

        double longitude = coordinates.longitude();
        double latitude = coordinates.latitude();

        boolean isLatitudeInRange = latitude >= -90 && latitude <= 90;
        boolean isLongitudeInRange = longitude >= -180 && longitude <= 180;
        if (!isLatitudeInRange || !isLongitudeInRange) {
            context.disableDefaultConstraintViolation();
            if (!isLatitudeInRange) {
                context.buildConstraintViolationWithTemplate("Latitude must be between -90.0 and 90.0")
                        .addPropertyNode("latitude")
                        .addConstraintViolation();
            }
            if (!isLongitudeInRange) {
                context.buildConstraintViolationWithTemplate("Longitude must be between -180.0 and 180.0")
                        .addPropertyNode("longitude")
                        .addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}
