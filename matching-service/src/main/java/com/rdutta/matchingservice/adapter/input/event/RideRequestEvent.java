package com.rdutta.matchingservice.adapter.input.event;

import com.rdutta.matchingservice.domain.dto.Location;

public record RideRequestEvent(
        String rideId,
        String riderId,
        Location pickupLocation,
        Location dropLocation
) {
}
