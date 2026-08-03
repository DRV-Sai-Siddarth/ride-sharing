package com.rdutta.matchingservice.adapter.output.kafka.producer;

import com.rdutta.matchingservice.adapter.input.event.RideMatchedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class RideMatchedProducer {

    public static final String RIDE_MATCHED = "ride.matched";

    private final KafkaTemplate<String, RideMatchedEvent> kafkaTemplate;

    public CompletableFuture<SendResult<String, RideMatchedEvent>> produce(
            RideMatchedEvent event) {

        return kafkaTemplate.send(
                RIDE_MATCHED,
                event.rideId(),
                event
        );
    }
}