package de.thi.focus.entities.events;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
