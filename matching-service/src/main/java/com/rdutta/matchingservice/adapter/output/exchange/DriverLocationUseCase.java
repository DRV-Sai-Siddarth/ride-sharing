package com.rdutta.matchingservice.adapter.output.exchange;

import com.rdutta.matchingservice.adapter.output.http.DriverLocationClient;
import com.rdutta.matchingservice.domain.dto.Location;
import reactor.core.publisher.Mono;

public interface DriverLocationUseCase {

    Mono<DriverLocationClient.DriverLocationResponse> findNearestDriver(
            Location pickupLocation,
            double radiusKm,
            int limit
    );
}
