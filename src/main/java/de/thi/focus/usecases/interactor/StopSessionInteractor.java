package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.usecases.dtos.input.StopSessionCommand;
import de.thi.focus.usecases.dtos.output.StopSessionOutputDTO;
import de.thi.focus.usecases.errors.SessionAccessDeniedException;
import de.thi.focus.usecases.errors.SessionNotFoundException;
import de.thi.focus.usecases.ports.inbound.StopSessionInputPort;
import de.thi.focus.usecases.ports.outbound.system.Clock;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class StopSessionInteractor implements StopSessionInputPort {

    private final FocusSessionRepository sessionRepository;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    public StopSessionInteractor(
            FocusSessionRepository sessionRepository,
            Clock clock,
            EventPublisher eventPublisher
    ) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.clock = Objects.requireNonNull(clock);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public StopSessionOutputDTO execute(StopSessionCommand command) {

        FocusSession session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new SessionNotFoundException(command.sessionId()));

        if (!session.getOwner().equals(command.userId())) {
            throw new SessionAccessDeniedException(command.userId(), command.sessionId());
        }

        Instant endedAt = command.endedAt() != null ? command.endedAt() : clock.now();

        session.stopAt(endedAt);

        sessionRepository.save(session);

        eventPublisher.publish(List.of());

        return new StopSessionOutputDTO(session.getId());
    }
}
