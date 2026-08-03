package com.rdutta.rideservice.domain.port.input;

import com.rdutta.rideservice.domain.dto.Location;
import com.rdutta.rideservice.domain.dto.RideRequest;
import reactor.core.publisher.Mono;

public interface CreateRideUseCase {

    /**
     * Creates and records a new ride request in the system.
     *
     * @param riderId Identifier of the rider creating the request
     * @param pickup Starting pickup coordinates
     * @param dropoff Destination coordinates
     * @return Mono containing the initialized RideRequest
     */
    Mono<RideRequest> createRide(String riderId, Location pickup, Location dropoff);
}