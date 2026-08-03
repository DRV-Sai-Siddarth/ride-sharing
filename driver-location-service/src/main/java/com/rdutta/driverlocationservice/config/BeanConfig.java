package com.rdutta.driverlocationservice.config;

import com.rdutta.driverlocationservice.domain.port.output.LocationEventPublisherPort;
import com.rdutta.driverlocationservice.domain.port.output.LocationRepositoryPort;
import com.rdutta.driverlocationservice.domain.service.DriverLocationApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public DriverLocationApplicationService driverLocationApplicationService(LocationRepositoryPort locationRepositoryPort, LocationEventPublisherPort locationEventPublisherPort) {
        return new DriverLocationApplicationService(locationRepositoryPort, locationEventPublisherPort);
    }
}
