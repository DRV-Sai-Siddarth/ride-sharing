package com.rdutta.rideservice.domain.dto;

import java.math.BigDecimal;

public record RideRequest(
        String rideId,
        String riderId,
        String driverId,
        Location pickupLocation,
        Location dropoffLocation,
        RideStatus status,
        BigDecimal finalAmount,
        Long version
) {
    /**
     * Factory constructor used when a rider initially requests a ride.
     * Starts in MATCHING state with null rideId (assigned on DB save) and null version.
     */
    public RideRequest(String riderId, Location pickupLocation, Location dropoffLocation, BigDecimal finalAmount) {
        this(
                null,
                riderId,
                null,
                pickupLocation,
                dropoffLocation,
                RideStatus.MATCHING,
                finalAmount,
                null
        );
    }

    /**
     * Transitions state when a driver is assigned by matching-service.
     */
    public RideRequest transitionTo(RideStatus newStatus, String driverId) {
        return new RideRequest(
                this.rideId,
                this.riderId,
                driverId,
                this.pickupLocation,
                this.dropoffLocation,
                newStatus,
                this.finalAmount,
                this.version // Carries existing version forward for optimistic locking checks
        );
    }
}