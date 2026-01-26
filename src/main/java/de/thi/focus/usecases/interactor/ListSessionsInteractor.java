package de.thi.focus.usecases.interactor;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.usecases.dtos.output.ListSessionsOutputDTO;
import de.thi.focus.usecases.ports.inbound.ListSessionsInputPort;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import java.util.List;
import java.util.Objects;

public final class ListSessionsInteractor implements ListSessionsInputPort {

    private final FocusSessionRepository sessionRepository;

    public ListSessionsInteractor(FocusSessionRepository sessionRepository) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
    }

    @Override
    public ListSessionsOutputDTO execute(UserId userId) {
        List<FocusSession> sessions = sessionRepository.findAllByUser(userId);

        List<ListSessionsOutputDTO.Item> items = sessions.stream()
                .map(s -> new ListSessionsOutputDTO.Item(
                        s.getId().toString(),
                        s.getTimeRange().getStart().toString(),
                        s.getTimeRange().getEnd() != null ? s.getTimeRange().getEnd().toString() : null,
                        s.getCategoryId() != null ? s.getCategoryId().toString() : null,
                        s.getNote() != null ? s.getNote().toString() : null
                ))
                .toList();

        return new ListSessionsOutputDTO(items);
    }
}
