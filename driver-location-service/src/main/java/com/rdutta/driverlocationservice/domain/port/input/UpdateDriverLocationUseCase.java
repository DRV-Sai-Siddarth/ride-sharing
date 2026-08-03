package com.rdutta.driverlocationservice.domain.port.input;

import com.rdutta.driverlocationservice.domain.dto.DriverLocation;
import reactor.core.publisher.Mono;

public interface UpdateDriverLocationUseCase {
    Mono<Void> updateLocation(DriverLocation location);
}
