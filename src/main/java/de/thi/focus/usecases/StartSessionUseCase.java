package de.thi.focus.usecases;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.valueobjects.Note;
import de.thi.focus.usecases.dtos.StartSessionCommand;
import de.thi.focus.usecases.errors.CategoryAccessDeniedException;
import de.thi.focus.usecases.errors.CategoryArchivedException;
import de.thi.focus.usecases.errors.CategoryNotFoundException;
import de.thi.focus.usecases.policies.RunningSessionPolicy;
import de.thi.focus.usecases.ports.CategoryRepository;
import de.thi.focus.usecases.ports.Clock;
import de.thi.focus.usecases.ports.EventPublisher;
import de.thi.focus.usecases.ports.FocusSessionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class StartSessionUseCase {

    private final FocusSessionRepository sessionRepository;
    private final CategoryRepository categoryRepository; // may be null if you decide to skip category validation
    private final RunningSessionPolicy runningSessionPolicy;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    public StartSessionUseCase(
            FocusSessionRepository sessionRepository,
            CategoryRepository categoryRepository,
            RunningSessionPolicy runningSessionPolicy,
            Clock clock,
            EventPublisher eventPublisher
    ) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.runningSessionPolicy = Objects.requireNonNull(runningSessionPolicy);
        this.clock = Objects.requireNonNull(clock);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    public FocusSessionId execute(StartSessionCommand command) {

        // Policy
        runningSessionPolicy.ensureNoRunningSession(command.userId());

        // Clock
        Instant startedAt = command.startedAt() != null ? command.startedAt() : clock.now();

        // Category
        CategoryId categoryId = command.categoryId();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryId));

            if (!category.getOwner().equals(command.userId())) {
                throw new CategoryAccessDeniedException(command.userId(), categoryId);
            }

            if (category.isArchived()) {
                throw new CategoryArchivedException(categoryId);
            }
        }

        // Create entity
        Note note = command.note() != null ? new Note(command.note()) : null;

        FocusSession session = FocusSession.start(
                FocusSessionId.newId(),
                command.userId(),
                startedAt,
                categoryId,
                note
        );

        // Persist
        sessionRepository.save(session);

        // TODO: Publish events
        eventPublisher.publish(List.of());

        return session.getId();
    }
}
