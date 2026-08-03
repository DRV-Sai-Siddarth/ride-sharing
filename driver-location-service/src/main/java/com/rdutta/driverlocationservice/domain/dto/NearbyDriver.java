package com.rdutta.driverlocationservice.domain.dto;

public record NearbyDriver(
        String driverId,
        Coordinates coordinates,
        double distanceInKm
) {
}
