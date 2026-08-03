package com.rdutta.driverlocationservice.adapter.output.redis;

import com.rdutta.driverlocationservice.domain.dto.Coordinates;
import com.rdutta.driverlocationservice.domain.dto.DriverLocation;
import com.rdutta.driverlocationservice.domain.dto.NearbyDriver;
import com.rdutta.driverlocationservice.domain.port.output.LocationRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisDriverLocationAdapter implements LocationRepositoryPort {
    private static final String GEO_KEY = "driver:locations";
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    /**
    * Calls every 3 second by driver's phone
    * and Map to Redis GEO_ADD Command
    */
    @Override
    public Mono<Void> updateDriverLocation(DriverLocation location) {
        log.info("Updating location for driver: {}", location.driverId());
        Point driverPoint = new Point(
                location.longitude(),
                location.latitude()
        );

        return reactiveRedisTemplate.opsForGeo().add(
                GEO_KEY,
                driverPoint,
                location.driverId()
        ).doOnSuccess((count) -> log.info("Updated location for driver: {}", location.driverId())).then();
    }

    /**
     * Find nearby drivers within given radius
     * , Called by Matching service on Ride request
     * , Maps to Redis GEO_RADIUS Command
     */
    @Override
    public Flux<NearbyDriver> searchNearby(Coordinates center, double radiusKm, int limit) {
        log.info("Searching drivers near lat: {} long: {} within {}Km", center.latitude(), center.longitude(), radiusKm);
        AtomicInteger count = new AtomicInteger();
        Circle searchArea = new Circle(
                new Point(
                        center.longitude(),
                        center.latitude()
                ),
                new Distance(radiusKm, RedisGeoCommands.DistanceUnit.KILOMETERS)
        );

        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeCoordinates()
                .includeDistance()
                .sortAscending()
                .limit(limit);

        return reactiveRedisTemplate.opsForGeo()
                .radius(
                        GEO_KEY,
                        searchArea,
                        args
                ).map(
                        geoResult -> {
                            RedisGeoCommands.GeoLocation<String> content = geoResult.getContent();
                            String driverId = content.getName();
                            Point point = content.getPoint();

                            double distanceInKm = geoResult.getDistance().getValue();

                            Coordinates driverCoords = new Coordinates(
                                    point.getX(), point.getY()
                            );

                            return new NearbyDriver(driverId, driverCoords, distanceInKm);
                        }
                )
                .doOnNext(driver -> count.incrementAndGet())
                .doOnComplete(() -> log.info("Found {} nearby drivers", count.get()));
    }


    /**
     * Remove driver when go offline
     * Map to Redis ZREM Command
     */
    @Override
    public Mono<Void> removeDriver(String driverId) {
        log.info("Removing driver {}", driverId);
        return reactiveRedisTemplate.opsForGeo().remove(
                GEO_KEY,
                driverId
        ).doOnSuccess(
                (c) -> log.info("Driver {} offline got removed", driverId)
        ).then();
    }
}
