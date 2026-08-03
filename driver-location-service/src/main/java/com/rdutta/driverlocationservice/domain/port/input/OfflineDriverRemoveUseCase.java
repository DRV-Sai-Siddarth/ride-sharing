package com.rdutta.driverlocationservice.domain.port.input;

import reactor.core.publisher.Mono;

public interface OfflineDriverRemoveUseCase {
    Mono<Void> removerDriver(String driverId);
}
