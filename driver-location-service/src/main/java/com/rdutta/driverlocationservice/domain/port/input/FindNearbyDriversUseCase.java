package com.rdutta.driverlocationservice.domain.port.input;

import com.rdutta.driverlocationservice.domain.dto.Coordinates;
import com.rdutta.driverlocationservice.domain.dto.NearbyDriver;
import reactor.core.publisher.Flux;

public interface FindNearbyDriversUseCase {
    Flux<NearbyDriver> findNearby(Coordinates center, double radiusKm, int limit);
}
