package de.thi.focus.usecases.ports.outbound;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.ids.FocusSessionId;

import java.util.Optional;

public interface FocusSessionRepository {

    Optional<FocusSession> findRunningByUser(UserId userId);

    Optional<FocusSession> findLastFinishedByUser(UserId userId);

    Optional<FocusSession> findById(FocusSessionId id);

    void save(FocusSession session);

    boolean existsByOwnerAndCategoryId(UserId ownerId, CategoryId categoryId);
}
