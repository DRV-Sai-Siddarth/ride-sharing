package com.rdutta.matchingservice.config;

import com.rdutta.matchingservice.adapter.output.exchange.DriverLocationUseCase;
import com.rdutta.matchingservice.adapter.output.kafka.producer.RideMatchedProducer;
import com.rdutta.matchingservice.domain.service.RideMatchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public RideMatchService rideMatchService(RideMatchedProducer rideMatchedProducer, DriverLocationUseCase driverLocationUseCase){
        return new RideMatchService(rideMatchedProducer, driverLocationUseCase);
    }
}
