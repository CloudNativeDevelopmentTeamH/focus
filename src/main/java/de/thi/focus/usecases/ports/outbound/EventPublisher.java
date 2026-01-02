package de.thi.focus.usecases.ports.outbound;

import de.thi.focus.entities.events.DomainEvent;

import java.util.List;

public interface EventPublisher {

    void publish(List<DomainEvent> events);
}
