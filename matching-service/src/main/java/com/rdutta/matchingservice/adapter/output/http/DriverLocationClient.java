package com.rdutta.matchingservice.adapter.output.http;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;

@HttpExchange("/api/v1/locations")
public interface DriverLocationClient {

    @GetExchange("/drivers/nearby")
    Flux<DriverLocationResponse> findNearbyDrivers(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam("radiusKm") double radiusKm,
            @RequestParam("limit") int limit
    );

    record DriverLocationResponse(
            String driverId,
            Coordinates coordinates,
            double distanceInKm
    ) {}

    record Coordinates(
            double longitude,
            double latitude
    ) {}
}