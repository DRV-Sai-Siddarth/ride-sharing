package com.rdutta.rideservice.adapter.utils;

import com.rdutta.rideservice.adapter.output.entity.Ride;
import com.rdutta.rideservice.domain.dto.RideRequest;
import com.rdutta.rideservice.domain.dto.RideStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RideMapper {

    public static Ride toRide(RideRequest rideRequest) {
        if (rideRequest == null) {
            return null;
        }

        String rideId = (rideRequest.rideId() != null) ? rideRequest.rideId() : UUID.randomUUID().toString();
        RideStatus status = (rideRequest.status() != null) ? rideRequest.status() : RideStatus.REQUESTED;

        return Ride.builder()
                .rideId(rideId)
                .riderId(rideRequest.riderId())
                .driverId(rideRequest.driverId())
                .pickupLocation(rideRequest.pickupLocation())
                .dropoffLocation(rideRequest.dropoffLocation())
                .status(status)
                .fareAmount(rideRequest.finalAmount())
                .version(rideRequest.version())
                .updatedAt(LocalDateTime.now())   // fine to set every time — not read-only
                .build();                          // createdAt intentionally omitted — @ReadOnlyProperty ignores it anyway
    }

    public static RideRequest toRideRequest(Ride ride) {
        if (ride == null) {
            return null;
        }

        return new RideRequest(
                ride.getRideId(),
                ride.getRiderId(),
                ride.getDriverId(),
                ride.getPickupLocation(),
                ride.getDropoffLocation(),
                ride.getStatus(),
                ride.getFareAmount(),
                ride.getVersion()
        );
    }
}