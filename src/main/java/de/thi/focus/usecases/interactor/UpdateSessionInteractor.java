package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.valueobjects.Note;
import de.thi.focus.usecases.dtos.input.UpdateSessionCommand;
import de.thi.focus.usecases.errors.SessionAccessDeniedException;
import de.thi.focus.usecases.errors.SessionNotFoundException;
import de.thi.focus.usecases.factories.FocusValueObjectFactory;
import de.thi.focus.usecases.ports.inbound.UpdateSessionInputPort;
import de.thi.focus.usecases.ports.outbound.EventPublisher;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import java.util.List;
import java.util.Objects;

public final class UpdateSessionInteractor implements UpdateSessionInputPort {

    private final FocusSessionRepository sessionRepository;
    private final FocusValueObjectFactory voFactory;
    private final EventPublisher eventPublisher;

    public UpdateSessionInteractor(
            FocusSessionRepository sessionRepository,
            FocusValueObjectFactory voFactory,
            EventPublisher eventPublisher
    ) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.voFactory = Objects.requireNonNull(voFactory);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public void execute(UpdateSessionCommand command) {

        FocusSession session = sessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new SessionNotFoundException(command.sessionId()));

        if (!session.getOwner().equals(command.userId())) {
            throw new SessionAccessDeniedException(command.userId(), command.sessionId());
        }

        // ---- NOTE ----
        boolean wantsClearNote = Boolean.TRUE.equals(command.clearNote());
        boolean wantsSetNote = command.note() != null;

        if (wantsClearNote && wantsSetNote) {
            throw new IllegalArgumentException("note and clearNote cannot be set at the same time");
        }

        if (wantsSetNote) {
            Note note = voFactory.note(command.note()); // applies configurable max length
            session.updateNote(note);
        } else if (wantsClearNote) {
            session.clearNote(); // sets nullable note to null
        }

        // ---- CATEGORY ----
        boolean wantsClearCategory = Boolean.TRUE.equals(command.clearCategory());
        boolean wantsSetCategory = command.categoryId() != null;

        if (wantsClearCategory && wantsSetCategory) {
            throw new IllegalArgumentException("categoryId and clearCategory cannot be set at the same time");
        }

        if (wantsSetCategory) {
            session.changeCategory(command.categoryId());
        } else if (wantsClearCategory) {
            session.clearCategory();
        }

        // startAt/endAt bewusst (noch) nicht implementiert

        sessionRepository.save(session);
        eventPublisher.publish(List.of());
    }
}
