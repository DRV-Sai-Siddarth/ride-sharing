package com.rdutta.rideservice.adapter.input.rest.request;

import com.rdutta.rideservice.domain.dto.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateRideHttpRequest(
        @NotBlank(message = "Rider ID is required")
        String riderId,

        @NotNull(message = "Pickup location is required")
        Location pickupLocation,

        @NotNull(message = "Dropoff location is required")
        Location dropoffLocation
) {
}