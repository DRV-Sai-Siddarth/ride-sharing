package com.rdutta.rideservice.adapter.input.kafka.event;

public record RideMatchedEvent(
        String rideId,
        String riderId,
        String driverId,
        double driverLatitude,
        double driverLongitude,
        double distanceToPickupKm
) {
}