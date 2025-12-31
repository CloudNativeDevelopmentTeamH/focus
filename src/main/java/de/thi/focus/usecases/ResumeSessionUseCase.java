package de.thi.focus.usecases;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.usecases.dtos.ResumeSessionCommand;
import de.thi.focus.usecases.errors.NoPreviousSessionToResumeException;
import de.thi.focus.usecases.policies.RunningSessionPolicy;
import de.thi.focus.usecases.ports.Clock;
import de.thi.focus.usecases.ports.FocusSessionRepository;

import java.time.Instant;
import java.util.Objects;

public final class ResumeSessionUseCase {

    private final FocusSessionRepository sessionRepository;
    private final RunningSessionPolicy runningSessionPolicy;
    private final Clock clock;

    public ResumeSessionUseCase(
            FocusSessionRepository sessionRepository,
            RunningSessionPolicy runningSessionPolicy,
            Clock clock
    ) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.runningSessionPolicy = Objects.requireNonNull(runningSessionPolicy);
        this.clock = Objects.requireNonNull(clock);
    }

    public FocusSessionId execute(ResumeSessionCommand command) {

        // 1) Enforce policy
        runningSessionPolicy.ensureNoRunningSession(command.userId());

        // 2) Load last finished session
        FocusSession previous = sessionRepository
                .findLastFinishedByUser(command.userId())
                .orElseThrow(() -> new NoPreviousSessionToResumeException(command.userId()));

        // 3) Determine start time
        Instant startedAt = command.startedAt() != null
                ? command.startedAt()
                : clock.now();

        // 4) Start new session (category reused, note intentionally empty)
        FocusSession resumed = FocusSession.start(
                FocusSessionId.newId(),
                command.userId(),
                startedAt,
                previous.getCategoryId(),
                null
        );

        // 5) Persist
        sessionRepository.save(resumed);

        return resumed.getId();
    }
}
