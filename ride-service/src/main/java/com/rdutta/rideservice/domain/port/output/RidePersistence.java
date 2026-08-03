package com.rdutta.rideservice.domain.port.output;

import com.rdutta.rideservice.domain.dto.RideRequest;
import com.rdutta.rideservice.domain.dto.RideStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RidePersistence {
    Mono<RideRequest> save(RideRequest rideRequest);
    Mono<RideRequest> findById(String rideId);
    Flux<RideRequest> findByRiderIdAndStatusIn(String riderId, RideStatus... statuses);
    Flux<RideRequest> findByDriverIdAndStatusIn(String driverId, RideStatus... statuses);
}
