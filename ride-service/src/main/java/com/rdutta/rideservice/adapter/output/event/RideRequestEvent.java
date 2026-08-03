package com.rdutta.rideservice.adapter.output.event;

import com.rdutta.rideservice.domain.dto.Location;

public record RideRequestEvent(
        String rideId,
        String riderId,
        Location pickupLocation,
        Location dropLocation
) {
}
