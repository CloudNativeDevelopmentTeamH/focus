package de.thi.focus.frameworksdrivers.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.thi.focus.entities.events.DomainEvent;
import de.thi.focus.entities.events.FocusSessionStopped;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import jakarta.annotation.PreDestroy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class KafkaEventPublisher implements EventPublisher {

    private static final String EVENT_TYPE_SESSION_ENDED = "FocusSessionEnded";

    private final Producer<String, String> producer;
    private final ObjectMapper objectMapper;
    private final String topic;

    public KafkaEventPublisher(String bootstrapServers, String topic, ObjectMapper objectMapper) {
        this.topic = requireNotBlank(topic, "topic");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, requireNotBlank(bootstrapServers, "bootstrapServers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void publish(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        for (DomainEvent event : events) {
            if (event instanceof FocusSessionStopped stopped) {
                publishSessionStopped(stopped);
            }
        }
    }

    private void publishSessionStopped(FocusSessionStopped event) {
        SessionEndedPayload payload = new SessionEndedPayload(
                event.sessionId().toString(),
                String.valueOf(event.userId().value()),
                event.categoryId() != null ? event.categoryId().toString() : null,
                event.duration().toSeconds(),
                event.endedAt().toString()
        );

        FocusEventEnvelope envelope = new FocusEventEnvelope(EVENT_TYPE_SESSION_ENDED, payload);

        String body;
        try {
            body = objectMapper.writeValueAsString(envelope);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("failed to serialize FocusSessionStopped event", e);
        }

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, payload.userId, body);

        try {
            producer.send(record).get();
        } catch (Exception e) {
            throw new IllegalStateException("failed to publish event to kafka", e);
        }
    }

    @PreDestroy
    void shutdown() {
        producer.close();
    }

    private static String requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    private static final class FocusEventEnvelope {
        public final String type;
        public final SessionEndedPayload sessionEnded;

        private FocusEventEnvelope(String type, SessionEndedPayload sessionEnded) {
            this.type = type;
            this.sessionEnded = sessionEnded;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static final class SessionEndedPayload {
        public final String sessionId;
        public final String userId;
        public final String categoryId;
        public final long durationSeconds;
        public final String endedAt;

        private SessionEndedPayload(
                String sessionId,
                String userId,
                String categoryId,
                long durationSeconds,
                String endedAt
        ) {
            this.sessionId = sessionId;
            this.userId = userId;
            this.categoryId = categoryId;
            this.durationSeconds = durationSeconds;
            this.endedAt = endedAt;
        }
    }
}
