package com.rdutta.rideservice.domain.port.input;

import com.rdutta.rideservice.domain.dto.RideRequest;
import com.rdutta.rideservice.domain.dto.RideStatus;
import reactor.core.publisher.Mono;

public interface UpdateRideStatusUseCase {
    /**
     * Transitions an active ride through its lifecycle state machine.
     *
     * @param rideId Identifier of the target ride
     * @param targetStatus Desired new state
     * @return Mono containing the updated RideRequest
     */
    Mono<RideRequest> updateStatus(String rideId, RideStatus targetStatus);
}
