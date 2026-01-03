package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.usecases.dtos.output.GetRunningSessionOutputDTO;
import de.thi.focus.usecases.ports.inbound.GetRunningSessionInputPort;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import java.util.Objects;

public final class GetRunningSessionInteractor implements GetRunningSessionInputPort {

    private final FocusSessionRepository sessionRepository;

    public GetRunningSessionInteractor(FocusSessionRepository sessionRepository) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
    }

    @Override
    public GetRunningSessionOutputDTO execute(UserId userId) {

        return sessionRepository.findRunningByUser(userId)
                .map(this::toOutput)
                .orElseGet(() -> new GetRunningSessionOutputDTO(false, null, null, null, null));
    }

    private GetRunningSessionOutputDTO toOutput(FocusSession s) {
        return new GetRunningSessionOutputDTO(
                true,
                s.getId().toString(),
                s.getTimeRange().getStart().toString(),
                s.getCategoryId() != null ? s.getCategoryId().toString() : null,
                s.getNote() != null ? s.getNote().toString() : null
        );
    }
}
