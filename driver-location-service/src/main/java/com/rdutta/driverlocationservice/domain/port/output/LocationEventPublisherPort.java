package com.rdutta.driverlocationservice.domain.port.output;

import com.rdutta.driverlocationservice.domain.dto.DriverLocation;
import reactor.core.publisher.Mono;

public interface LocationEventPublisherPort {
    Mono<Void> publishLocationUpdatedEvent(DriverLocation location);
}
