package com.rdutta.rideservice.domain.port.input;

import com.rdutta.rideservice.domain.dto.RideRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetRideQueryUseCase {
    Mono<RideRequest> getRideById(String rideId);
    Flux<RideRequest> getActiveRidesForRider(String riderId);
    Flux<RideRequest> getActiveRidesForDriver(String driverId);
}
