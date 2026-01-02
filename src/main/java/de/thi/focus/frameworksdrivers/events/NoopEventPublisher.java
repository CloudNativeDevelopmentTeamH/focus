package de.thi.focus.frameworksdrivers.events;

import de.thi.focus.entities.events.DomainEvent;
import de.thi.focus.usecases.ports.outbound.EventPublisher;

import java.util.List;

public final class NoopEventPublisher implements EventPublisher {

    @Override
    public void publish(List<DomainEvent> events) {
        // intentionally no-op
    }
}
