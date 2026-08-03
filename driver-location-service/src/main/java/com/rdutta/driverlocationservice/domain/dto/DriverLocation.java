package com.rdutta.driverlocationservice.domain.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record DriverLocation(
        @NotBlank(message = "Driver ID cannot be blank")
        String driverId,

        @DecimalMin(value = "-85.05112878", message = "Latitude must be between -85.05112878 and 85.05112878")
        @DecimalMax(value = "85.05112878", message = "Latitude must be between -85.05112878 and 85.05112878")
        double latitude,

        @DecimalMin(value = "-180.0", message = "Longitude must be between -180.0 and 180.0")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180.0 and 180.0")
        double longitude,
         float heading,
         float speed,
         Instant timestamp
) {
  public DriverLocation {
      if (timestamp == null) {
          timestamp = Instant.now();
      }
  }
    public DriverLocation(String driverId, double latitude, double longitude, float heading, float speed) {
        this(driverId, latitude, longitude, heading, speed, Instant.now());
    }
}
