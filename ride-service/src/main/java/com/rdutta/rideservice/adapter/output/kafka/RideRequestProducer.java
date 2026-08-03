package com.rdutta.rideservice.adapter.output.kafka;

import com.rdutta.rideservice.adapter.output.event.RideRequestEvent;
import com.rdutta.rideservice.domain.dto.RideRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class RideRequestProducer {
    private final String TOPIC = "ride.requested";
    private final KafkaTemplate<String, RideRequestEvent> kafkaTemplate;


    /**
     * Maps the RideRequest DTO to a RideRequestEvent and publishes it to Kafka.
     *
     * @param rideRequest The domain request DTO
     * @return CompletableFuture containing the SendResult
     */
    public CompletableFuture<SendResult<String, RideRequestEvent>> produceEvent(RideRequest rideRequest) {
        RideRequestEvent event = new RideRequestEvent(
                rideRequest.rideId(),
                rideRequest.riderId(),
                rideRequest.pickupLocation(),
                rideRequest.dropoffLocation()
        );

        String messageKey = rideRequest.rideId();
        log.info("Publishing RideRequestEvent to topic '{}' with key '{}'", TOPIC, messageKey);

        CompletableFuture<SendResult<String, RideRequestEvent>> future =
                kafkaTemplate.send(TOPIC, messageKey, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully published RideRequestEvent [rideId={}] to partition {} at offset {}",
                        rideRequest.rideId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish RideRequestEvent [rideId={}]: {}",
                        rideRequest.rideId(), ex.getMessage(), ex);
            }
        });

        return future;
    }
}
