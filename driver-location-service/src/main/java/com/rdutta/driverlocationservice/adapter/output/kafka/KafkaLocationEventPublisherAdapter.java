package com.rdutta.driverlocationservice.adapter.output.kafka;

import com.rdutta.driverlocationservice.domain.dto.DriverLocation;
import com.rdutta.driverlocationservice.domain.port.output.LocationEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaLocationEventPublisherAdapter implements LocationEventPublisherPort {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "driver-location-updates";
    @Override
    public Mono<Void> publishLocationUpdatedEvent(DriverLocation location) {
        return Mono.defer(() -> {
            try {
                String payload = objectMapper.writeValueAsString(location);

                ProducerRecord<String, String> record = new ProducerRecord<>(
                        TOPIC,
                        location.driverId(),
                        payload
                );

                return Mono.fromFuture(kafkaTemplate.send(record))
                        .doOnSuccess(result -> log.debug("Published location event for driver {} to partition {}",
                                location.driverId(), Objects.requireNonNull(result).getRecordMetadata().partition()))
                        .doOnError(ex -> log.error("Failed to publish location event for driver {}", location.driverId(), ex))
                        .then();

            } catch (Exception e) {
                log.error("Error serializing DriverLocation payload for driverId: {}", location.driverId(), e);
                return Mono.error(e);
            }

        });
    }
}
