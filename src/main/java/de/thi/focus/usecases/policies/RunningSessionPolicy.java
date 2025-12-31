package de.thi.focus.usecases.policies;

import de.thi.focus.entities.ids.UserId;
import de.thi.focus.usecases.errors.RunningSessionAlreadyExistsException;
import de.thi.focus.usecases.ports.FocusSessionRepository;

import java.util.Objects;

public final class RunningSessionPolicy {

    private final FocusSessionRepository sessionRepository;

    public RunningSessionPolicy(FocusSessionRepository sessionRepository) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
    }

    public void ensureNoRunningSession(UserId userId) {
        sessionRepository.findRunningByUser(userId)
                .ifPresent(session -> {
                    throw new RunningSessionAlreadyExistsException(userId);
                });
    }
}
