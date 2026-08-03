package com.rdutta.rideservice.adapter.input.rest;

import com.rdutta.rideservice.adapter.input.rest.request.CreateRideHttpRequest;
import com.rdutta.rideservice.domain.dto.RideRequest;
import com.rdutta.rideservice.domain.dto.RideStatus;
import com.rdutta.rideservice.domain.port.input.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final CreateRideUseCase createRideUseCasePort;
    private final AssignDriverUseCase assignDriverUseCasePort;
    private final UpdateRideStatusUseCase updateRideStatusUseCasePort;
    private final GetRideQueryUseCase getRideQueryPort;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RideRequest> createRide(@Valid @RequestBody CreateRideHttpRequest request) {
        return createRideUseCasePort.createRide(
                request.riderId(),
                request.pickupLocation(),
                request.dropoffLocation()
        );
    }

    @GetMapping("/{rideId}")
    public Mono<RideRequest> getRideById(@PathVariable String rideId) {
        return getRideQueryPort.getRideById(rideId);
    }

    @PatchMapping("/{rideId}/assign")
    public Mono<RideRequest> assignDriver(
            @PathVariable String rideId,
            @RequestParam String driverId) {
        return assignDriverUseCasePort.assignDriver(rideId, driverId);
    }

    @PatchMapping("/{rideId}/status")
    public Mono<RideRequest> updateStatus(
            @PathVariable String rideId,
            @RequestParam RideStatus status) {
        return updateRideStatusUseCasePort.updateStatus(rideId, status);
    }

    @GetMapping("/active/rider/{riderId}")
    public Flux<RideRequest> getActiveRidesForRider(@PathVariable String riderId) {
        return getRideQueryPort.getActiveRidesForRider(riderId);
    }
}