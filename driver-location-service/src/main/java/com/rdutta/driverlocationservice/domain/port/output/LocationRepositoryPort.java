package com.rdutta.driverlocationservice.domain.port.output;

import com.rdutta.driverlocationservice.domain.dto.Coordinates;
import com.rdutta.driverlocationservice.domain.dto.DriverLocation;
import com.rdutta.driverlocationservice.domain.dto.NearbyDriver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LocationRepositoryPort {
    Mono<Void> updateDriverLocation(DriverLocation location);
    Flux<NearbyDriver> searchNearby(Coordinates center, double radiusKm, int limit);
    Mono<Void> removeDriver(String driverId);
}
