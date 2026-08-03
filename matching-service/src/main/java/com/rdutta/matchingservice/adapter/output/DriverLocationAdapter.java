package com.rdutta.matchingservice.adapter.output;

import com.rdutta.matchingservice.adapter.output.exception.DriverNotFoundException;
import com.rdutta.matchingservice.adapter.output.exchange.DriverLocationUseCase;
import com.rdutta.matchingservice.adapter.output.http.DriverLocationClient;
import com.rdutta.matchingservice.domain.dto.Location;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverLocationAdapter implements DriverLocationUseCase {

    private final DriverLocationClient driverLocationClient;

    @CircuitBreaker(
            name = "driverLocationService",
            fallbackMethod = "findNearestDriverFallback"
    )
    @Override
    public Mono<DriverLocationClient.DriverLocationResponse> findNearestDriver(
            Location pickupLocation,
            double radiusKm,
            int limit
    ) {

        if (pickupLocation == null) {
            return Mono.empty();
        }

        return driverLocationClient
                .findNearbyDrivers(
                        pickupLocation.latitude(),
                        pickupLocation.longitude(),
                        radiusKm,
                        limit
                )
                .next()
                .timeout(Duration.ofSeconds(2))
                .retryWhen(
                        Retry.backoff(2, Duration.ofMillis(300))
                                .filter(ex -> ex instanceof TimeoutException)
                )
                .doOnError(ex ->
                        log.warn("Driver lookup failed: {}", ex.getMessage()));
    }

    public Mono<DriverLocationClient.DriverLocationResponse> findNearestDriverFallback(
            Location pickupLocation,
            double radiusKm,
            int limit,
            Throwable ex
    ) {

        log.warn("Circuit breaker opened: {}", ex.getMessage());

        return Mono.error(
                new DriverNotFoundException(
                        "Driver location service unavailable"
                )
        );
    }
}