package com.rdutta.matchingservice.domain.service;

import com.rdutta.matchingservice.adapter.input.event.RideMatchedEvent;
import com.rdutta.matchingservice.adapter.input.event.RideRequestEvent;
import com.rdutta.matchingservice.adapter.output.exception.DriverNotFoundException;
import com.rdutta.matchingservice.adapter.output.exchange.DriverLocationUseCase;
import com.rdutta.matchingservice.adapter.output.kafka.producer.RideMatchedProducer;
import com.rdutta.matchingservice.domain.port.input.RideMatchUseCase;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;

public class RideMatchService implements RideMatchUseCase {
    private static final double SEARCH_RADIUS_KM = 5.0;
    private final RideMatchedProducer rideMatchedProducer;
    private final DriverLocationUseCase driverLocationUseCase;

    public RideMatchService(RideMatchedProducer rideMatchedProducer, DriverLocationUseCase driverLocationUseCase) {
        this.rideMatchedProducer = rideMatchedProducer;
        this.driverLocationUseCase = driverLocationUseCase;
    }

    @Override
    public Mono<Void> matchDriverForRide(RideRequestEvent event) {

        return driverLocationUseCase
                .findNearestDriver(
                        event.pickupLocation(),
                        SEARCH_RADIUS_KM,
                        10
                )
                .switchIfEmpty(
                        Mono.error(new DriverNotFoundException("No nearby driver found"))
                )
                .flatMap(driver -> {

                    RideMatchedEvent matched = new RideMatchedEvent(
                            event.rideId(),
                            event.riderId(),
                            driver.driverId(),
                            driver.coordinates().latitude(),
                            driver.coordinates().longitude(),
                            driver.distanceInKm()
                    );

                    return Mono.fromFuture(
                                    rideMatchedProducer.produce(
                                            matched
                                    )
                            )
                            .doOnSuccess(result ->
                                    System.out.println("Ride matched successfully: " + event.rideId()))
                            .doOnError(error ->
                                    System.err.println("Failed to publish ride matched event: " + error.getMessage()));
                })
                .then();
    }
}
