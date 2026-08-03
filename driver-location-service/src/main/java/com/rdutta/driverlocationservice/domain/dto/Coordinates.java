package com.rdutta.driverlocationservice.domain.dto;


import com.rdutta.driverlocationservice.domain.annotation.ValidateCoordinates;

@ValidateCoordinates
public record Coordinates(
        double longitude,
        double latitude
) {
}
