package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.usecases.dtos.input.ResumeSessionCommand;
import de.thi.focus.usecases.dtos.output.ResumeSessionOutputDTO;
import de.thi.focus.usecases.errors.SessionAccessDeniedException;
import de.thi.focus.usecases.errors.SessionNotFoundException;
import de.thi.focus.usecases.policies.RunningSessionPolicy;
import de.thi.focus.usecases.ports.inbound.ResumeSessionInputPort;
import de.thi.focus.usecases.ports.outbound.system.Clock;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class ResumeSessionInteractor implements ResumeSessionInputPort {

    private final FocusSessionRepository sessionRepository;
    private final RunningSessionPolicy runningSessionPolicy;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    public ResumeSessionInteractor(
            FocusSessionRepository sessionRepository,
            RunningSessionPolicy runningSessionPolicy,
            Clock clock,
            EventPublisher eventPublisher
    ) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.runningSessionPolicy = Objects.requireNonNull(runningSessionPolicy);
        this.clock = Objects.requireNonNull(clock);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public ResumeSessionOutputDTO execute(ResumeSessionCommand command) {

        runningSessionPolicy.ensureNoRunningSession(command.userId());

        FocusSession previous = sessionRepository.findById(command.previousSessionId())
                .orElseThrow(() -> new SessionNotFoundException(command.previousSessionId()));

        if (!previous.getOwner().equals(command.userId())) {
            throw new SessionAccessDeniedException(command.userId(), command.previousSessionId());
        }

        if (previous.isRunning()) {
            throw new de.thi.focus.entities.errors.SessionStillRunningException(previous.getId());
        }

        Instant startedAt = command.startedAt() != null ? command.startedAt() : clock.now();

        FocusSession resumed = FocusSession.start(
                FocusSessionId.newId(),
                command.userId(),
                startedAt,
                previous.getCategoryId(),
                null
        );

        sessionRepository.save(resumed);

        eventPublisher.publish(List.of());

        return new ResumeSessionOutputDTO(resumed.getId(), previous.getId());
    }
}
