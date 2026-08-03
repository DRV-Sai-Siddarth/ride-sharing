package com.rdutta.rideservice.adapter.output;

import com.rdutta.rideservice.adapter.output.entity.Ride;
import com.rdutta.rideservice.adapter.output.jpa.R2dbcRideRepository;
import com.rdutta.rideservice.adapter.utils.RideMapper;
import com.rdutta.rideservice.domain.dto.RideRequest;
import com.rdutta.rideservice.domain.dto.RideStatus;
import com.rdutta.rideservice.domain.port.output.RidePersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RidePersistenceAdapter implements RidePersistence {
    private final R2dbcRideRepository  rideRepository;

    @Transactional
    @Override
    public Mono<RideRequest> save(RideRequest rideRequest) {
        Ride ride = RideMapper.toRide(rideRequest);
        return rideRepository.save(ride).map(
                RideMapper::toRideRequest
        );
    }

    @Override
    public Mono<RideRequest> findById(String rideId) {
        return rideRepository.findById(rideId).map(
                RideMapper::toRideRequest
        );
    }

    @Override
    public Flux<RideRequest> findByRiderIdAndStatusIn(String riderId, RideStatus... statuses) {
        return rideRepository.findByRiderIdAndStatusIn(riderId, List.of(statuses))
                .map(RideMapper::toRideRequest);
    }

    @Override
    public Flux<RideRequest> findByDriverIdAndStatusIn(String driverId, RideStatus... statuses) {
        return rideRepository.findByDriverIdAndStatusIn(driverId, List.of(statuses))
                .map(RideMapper::toRideRequest);
    }
}
