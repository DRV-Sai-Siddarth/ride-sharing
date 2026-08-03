package com.rdutta.driverlocationservice.adapter.input.rest;

import com.rdutta.driverlocationservice.domain.dto.Coordinates;
import com.rdutta.driverlocationservice.domain.dto.DriverLocation;
import com.rdutta.driverlocationservice.domain.dto.NearbyDriver;
import com.rdutta.driverlocationservice.domain.port.input.FindNearbyDriversUseCase;
import com.rdutta.driverlocationservice.domain.port.input.OfflineDriverRemoveUseCase;
import com.rdutta.driverlocationservice.domain.port.input.UpdateDriverLocationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class DriverLocationRestController {

    private final UpdateDriverLocationUseCase updateDriverLocationUseCase;
    private final FindNearbyDriversUseCase findNearbyDriversUseCase;
    private final OfflineDriverRemoveUseCase offlineDriverRemoveUseCase;

    @PostMapping("/drivers/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> updateLocation(@RequestBody @Valid DriverLocation driverLocation) {
        return updateDriverLocationUseCase.updateLocation(driverLocation)
                .then(Mono.just("Successfully updated location"));
    }

    @GetMapping("/drivers/nearby")
    @ResponseStatus(HttpStatus.OK)
    public Flux<NearbyDriver> getNearbyDrivers(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5.0") double radiusKm,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return findNearbyDriversUseCase.findNearby(new Coordinates(lon, lat), radiusKm, limit);
    }

    @DeleteMapping("/drivers/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeOfflineDriver(@PathVariable String driverId) {
        return offlineDriverRemoveUseCase.removerDriver(driverId);
    }
}