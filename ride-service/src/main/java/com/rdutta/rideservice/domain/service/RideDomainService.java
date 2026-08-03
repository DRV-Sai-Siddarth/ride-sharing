package com.rdutta.rideservice.domain.service;

import com.rdutta.rideservice.adapter.output.kafka.RideRequestProducer;
import com.rdutta.rideservice.domain.dto.Location;
import com.rdutta.rideservice.domain.dto.RideRequest;
import com.rdutta.rideservice.domain.dto.RideStatus;
import com.rdutta.rideservice.domain.port.input.AssignDriverUseCase;
import com.rdutta.rideservice.domain.port.input.CreateRideUseCase;
import com.rdutta.rideservice.domain.port.input.GetRideQueryUseCase;
import com.rdutta.rideservice.domain.port.input.UpdateRideStatusUseCase;
import com.rdutta.rideservice.domain.port.output.RidePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class RideDomainService implements
        CreateRideUseCase,
        GetRideQueryUseCase,
        UpdateRideStatusUseCase,
        AssignDriverUseCase {

    private final RidePersistence ridePersistence;
    private final RideRequestProducer rideRequestProducer;
    private final Logger log = LoggerFactory.getLogger(RideDomainService.class);
    private static final double BASE_FARE = 2.50;
    private static final double RATE_PER_KM = 1.20;


    public RideDomainService(RidePersistence ridePersistence, RideRequestProducer rideRequestProducer) {
        this.ridePersistence = ridePersistence;
        this.rideRequestProducer = rideRequestProducer;
    }

    @Override
    @Transactional
    public Mono<RideRequest> createRide(String riderId, Location pickup, Location dropoff) {

        log.info("Step 1 - Create ride request");

        Mono<BigDecimal> surgeMultiplierMono = Mono.just(BigDecimal.ONE);

        return surgeMultiplierMono.flatMap(surgeMultiplier -> {

            log.info("Step 2 - Calculating fare");

            BigDecimal basePrice = calculateBasePrice(pickup, dropoff);
            BigDecimal finalFare = basePrice.multiply(surgeMultiplier);

            RideRequest initialRequest =
                    new RideRequest(riderId, pickup, dropoff, finalFare);

            log.info("Step 3 - Saving ride in MATCHING state");

            return ridePersistence.save(initialRequest)
                    .doOnNext(r -> log.info("Ride saved: {}, status: {}", r.rideId(), r.status()))
                    .flatMap(savedRide -> {

                        log.info("Step 4 - Emitting RideRequestEvent to Kafka for matching-service");

                        return Mono.fromFuture(() -> rideRequestProducer.produceEvent(savedRide))
                                // Return savedRide directly once the Kafka event is dispatched
                                .thenReturn(savedRide);
                    })
                    .doOnError(ex -> log.error("Ride creation failed", ex));
        });
    }

    @Override
    @Transactional
    public Mono<RideRequest> assignDriver(String rideId, String driverId) {
        return ridePersistence.findById(rideId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Ride not found with id: " + rideId)))
                .flatMap(ride -> {
                    // Domain assertion: Can only assign driver if ride is in REQUESTED or MATCHING state
                    if (ride.status() != RideStatus.REQUESTED && ride.status() != RideStatus.MATCHING) {
                        return Mono.error(new IllegalStateException("Cannot assign driver to ride in state: " + ride.status()));
                    }

                    // Transition state and assign driver
                    RideRequest updatedRide = ride.transitionTo(RideStatus.DRIVER_ARRIVING, driverId);
                    return ridePersistence.save(updatedRide);
                });
    }

    @Override
    @Transactional
    public Mono<RideRequest> updateStatus(String rideId, RideStatus targetStatus) {
        return ridePersistence.findById(rideId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Ride not found with id: " + rideId)))
                .flatMap(ride -> {
                    RideRequest updatedRide = ride.transitionTo(targetStatus, ride.driverId());
                    return ridePersistence.save(updatedRide);
                });
    }

    @Override
    public Mono<RideRequest> getRideById(String rideId) {
        return ridePersistence.findById(rideId);
    }

    @Override
    public Flux<RideRequest> getActiveRidesForRider(String riderId) {
        return ridePersistence.findByRiderIdAndStatusIn(
                riderId,
                RideStatus.REQUESTED,
                RideStatus.MATCHING,
                RideStatus.DRIVER_ARRIVING,
                RideStatus.RIDE_STARTED
        );
    }

    @Override
    public Flux<RideRequest> getActiveRidesForDriver(String driverId) {
        return ridePersistence.findByDriverIdAndStatusIn(
                driverId,
                RideStatus.DRIVER_ARRIVING,
                RideStatus.RIDE_STARTED
        );
    }

    private BigDecimal calculateBasePrice(Location pickup, Location dropoff) {
        double distanceKm = calculateDistance(pickup, dropoff);
        double price = BASE_FARE + (distanceKm * RATE_PER_KM);
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    private double calculateDistance(Location a, Location b) {
        if (a == null || b == null) return 0.0;

        final int R = 6371; // Earth radius in km
        double latDist = Math.toRadians(b.latitude() - a.latitude());
        double lonDist = Math.toRadians(b.longitude() - a.longitude());

        double h = Math.sin(latDist / 2) * Math.sin(latDist / 2)
                + Math.cos(Math.toRadians(a.latitude())) * Math.cos(Math.toRadians(b.latitude()))
                * Math.sin(lonDist / 2) * Math.sin(lonDist / 2);

        double c = 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));
        return R * c;
    }
}