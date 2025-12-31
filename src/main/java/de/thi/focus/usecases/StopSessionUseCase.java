package de.thi.focus.usecases;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.usecases.dtos.StopSessionCommand;
import de.thi.focus.usecases.errors.SessionAccessDeniedException;
import de.thi.focus.usecases.errors.SessionNotFoundException;
import de.thi.focus.usecases.ports.Clock;
import de.thi.focus.usecases.ports.EventPublisher;
import de.thi.focus.usecases.ports.FocusSessionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class StopSessionUseCase {

    private final FocusSessionRepository sessionRepository;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    public StopSessionUseCase(
            FocusSessionRepository sessionRepository,
            Clock clock,
            EventPublisher eventPublisher
    ) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.clock = Objects.requireNonNull(clock);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    public void execute(StopSessionCommand command) {

        // Load session
        FocusSession session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new SessionNotFoundException(command.sessionId()));

        // Ownership check
        if (!session.getOwner().equals(command.userId())) {
            throw new SessionAccessDeniedException(command.userId(), command.sessionId());
        }

        // Determine endedAt
        Instant endedAt = command.endedAt() != null ? command.endedAt() : clock.now();

        // Mutate session
        session.stopAt(endedAt);

        // Persist
        sessionRepository.save(session);

        // TODO: Publish events
        eventPublisher.publish(List.of());
    }
}
