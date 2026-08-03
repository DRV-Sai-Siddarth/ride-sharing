package com.rdutta.matchingservice.adapter.input.kafka.consumer;

import com.rdutta.matchingservice.adapter.input.event.RideRequestEvent;
import com.rdutta.matchingservice.domain.service.RideMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RideRequestConsumer {
    private final RideMatchService service;

    @KafkaListener(
            topics = "ride.requested",
            groupId = "matching-service-group",
            containerFactory = "rideRequestEventConcurrentKafkaListenerContainerFactory"
    )
    public void consumeRideRequestedEvent(RideRequestEvent event) {
        try{
            service.matchDriverForRide(event)
                    .subscribe(
                            unused -> {},
                            ex -> log.error(
                                    "Failed matching ride {}",
                                    event.rideId(),
                                    ex
                            )
                    );
        }catch (Exception e){
            log.error("Error processing ride request: {} - {}",
                    event.rideId(), e.getMessage());
        }
    }
}
