package com.rdutta.driverlocationservice.domain.service;

import com.rdutta.driverlocationservice.domain.dto.Coordinates;
import com.rdutta.driverlocationservice.domain.dto.DriverLocation;
import com.rdutta.driverlocationservice.domain.dto.NearbyDriver;
import com.rdutta.driverlocationservice.domain.port.input.FindNearbyDriversUseCase;
import com.rdutta.driverlocationservice.domain.port.input.OfflineDriverRemoveUseCase;
import com.rdutta.driverlocationservice.domain.port.input.UpdateDriverLocationUseCase;
import com.rdutta.driverlocationservice.domain.port.output.LocationEventPublisherPort;
import com.rdutta.driverlocationservice.domain.port.output.LocationRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DriverLocationApplicationService implements FindNearbyDriversUseCase, UpdateDriverLocationUseCase, OfflineDriverRemoveUseCase {
    private final LocationRepositoryPort locationRepositoryPort;
    private final LocationEventPublisherPort locationEventPublisherPort;

    public DriverLocationApplicationService(LocationRepositoryPort locationRepositoryPort, LocationEventPublisherPort locationEventPublisherPort) {
        this.locationRepositoryPort = locationRepositoryPort;
        this.locationEventPublisherPort = locationEventPublisherPort;
    }

    @Override
    public Flux<NearbyDriver> findNearby(Coordinates center, double radiusKm, int limit) {
        return locationRepositoryPort.searchNearby(center, radiusKm, limit);
    }

    @Override
    public Mono<Void> updateLocation(DriverLocation location) {
        return locationRepositoryPort.updateDriverLocation(location)
                .then(
                        locationEventPublisherPort.publishLocationUpdatedEvent(location)
                );
    }

    @Override
    public Mono<Void> removerDriver(String driverId) {
        return locationRepositoryPort.removeDriver(driverId);
    }
}
