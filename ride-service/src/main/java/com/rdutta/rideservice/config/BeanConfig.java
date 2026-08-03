package com.rdutta.rideservice.config;

import com.rdutta.rideservice.adapter.output.kafka.RideRequestProducer;
import com.rdutta.rideservice.domain.port.output.RidePersistence;
import com.rdutta.rideservice.domain.service.RideDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RideDomainService rideDomainService(RidePersistence ridePersistence, RideRequestProducer rideRequestProducer) {
        return new RideDomainService(ridePersistence, rideRequestProducer);
    }
}
