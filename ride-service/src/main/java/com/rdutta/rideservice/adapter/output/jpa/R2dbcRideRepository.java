package com.rdutta.rideservice.adapter.output.jpa;

import com.rdutta.rideservice.adapter.output.entity.Ride;
import com.rdutta.rideservice.domain.dto.RideStatus;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Repository
public interface R2dbcRideRepository extends R2dbcRepository<Ride, String> {
    Flux<Ride> findByRiderIdAndStatusIn(String riderId, Collection<RideStatus> statuses);

    Flux<Ride> findByDriverIdAndStatusIn(String driverId, Collection<RideStatus> statuses);
}
