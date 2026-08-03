package com.rdutta.rideservice.adapter.input.kafka.consumer;

import com.rdutta.rideservice.adapter.input.kafka.event.RideMatchedEvent;
import com.rdutta.rideservice.domain.port.input.AssignDriverUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RideMatchedConsumer {

    private final AssignDriverUseCase assignDriverUseCase;

    @KafkaListener(
            topics = "ride.matched",
            groupId = "ride-service-group",
            containerFactory = "rideRequestEventConcurrentKafkaListenerContainerFactory"
    )
    public void consume(RideMatchedEvent event) {

        assignDriverUseCase
                .assignDriver(
                        event.rideId(),
                        event.driverId()
                )
                .subscribe(
                        ride -> log.info(
                                "Driver {} assigned to ride {}",
                                event.driverId(),
                                event.rideId()
                        ),
                        ex -> log.error(
                                "Assignment failed",
                                ex
                        )
                );
    }
}