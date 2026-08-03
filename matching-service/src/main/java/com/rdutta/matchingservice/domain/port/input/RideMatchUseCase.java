package com.rdutta.matchingservice.domain.port.input;

import com.rdutta.matchingservice.adapter.input.event.RideRequestEvent;
import reactor.core.publisher.Mono;

public interface RideMatchUseCase {
    Mono<Void> matchDriverForRide(RideRequestEvent event);
}
