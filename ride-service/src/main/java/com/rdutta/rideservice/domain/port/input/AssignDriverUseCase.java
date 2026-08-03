package com.rdutta.rideservice.domain.port.input;

import com.rdutta.rideservice.domain.dto.RideRequest;
import reactor.core.publisher.Mono;

public interface AssignDriverUseCase {
    /**
     * Atomically assigns a driver to an active ride and updates its status to DRIVER_ASSIGNED.
     *
     * @param rideId Identifier of the ride to assign
     * @param driverId Identifier of the driver accepting the assignment
     * @return Mono containing the updated RideRequest record
     */
    Mono<RideRequest> assignDriver(String rideId, String driverId);
}
