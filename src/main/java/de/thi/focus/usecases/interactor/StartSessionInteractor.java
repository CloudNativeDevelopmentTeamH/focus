package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.valueobjects.Note;
import de.thi.focus.usecases.dtos.input.StartSessionCommand;
import de.thi.focus.usecases.dtos.output.StartSessionOutputDTO;
import de.thi.focus.usecases.errors.CategoryAccessDeniedException;
import de.thi.focus.usecases.errors.CategoryArchivedException;
import de.thi.focus.usecases.errors.CategoryNotFoundException;
import de.thi.focus.usecases.policies.RunningSessionPolicy;
import de.thi.focus.usecases.ports.inbound.StartSessionInputPort;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import de.thi.focus.usecases.ports.outbound.system.Clock;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;
import de.thi.focus.config.FocusConstraintsConfig;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class StartSessionInteractor implements StartSessionInputPort {

    private final FocusSessionRepository sessionRepository;
    private final CategoryRepository categoryRepository;
    private final RunningSessionPolicy runningSessionPolicy;
    private final Clock clock;
    private final EventPublisher eventPublisher;
    private final FocusConstraintsConfig constraints;

    public StartSessionInteractor(
            FocusSessionRepository sessionRepository,
            CategoryRepository categoryRepository,
            RunningSessionPolicy runningSessionPolicy,
            Clock clock,
            EventPublisher eventPublisher,
            FocusConstraintsConfig constraints
    ) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.runningSessionPolicy = Objects.requireNonNull(runningSessionPolicy);
        this.clock = Objects.requireNonNull(clock);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
        this.constraints = Objects.requireNonNull(constraints);
    }

    @Override
    public StartSessionOutputDTO execute(StartSessionCommand command) {

        runningSessionPolicy.ensureNoRunningSession(command.userId());

        Instant startedAt = command.startedAt() != null ? command.startedAt() : clock.now();

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

        Note note = (command.note() != null)
                ? Note.of(command.note(), constraints.note().maxLength())
                : null;

        FocusSession session = FocusSession.start(
                FocusSessionId.newId(),
                command.userId(),
                startedAt,
                categoryId,
                note
        );

        sessionRepository.save(session);

        eventPublisher.publish(List.of());

        return new StartSessionOutputDTO(session.getId());
    }
}
