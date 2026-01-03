package de.thi.focus.frameworksdrivers.persistence;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.usecases.ports.outbound.FocusSessionRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory repository for local dev & tests.
 * Not intended for production use.
 */
public final class InMemoryFocusSessionRepository implements FocusSessionRepository {

    private final ConcurrentHashMap<FocusSessionId, FocusSession> store = new ConcurrentHashMap<>();

    @Override
    public Optional<FocusSession> findRunningByUser(UserId userId) {
        return store.values().stream()
                .filter(s -> s.getOwner().equals(userId))
                .filter(FocusSession::isRunning)
                .findFirst();
    }

    @Override
    public Optional<FocusSession> findById(FocusSessionId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<FocusSession> findLastFinishedByUser(UserId userId) {
        return store.values().stream()
                .filter(s -> s.getOwner().equals(userId))
                .filter(s -> !s.isRunning())
                .max(Comparator.comparing(this::finishedAtSafe));
    }

    private Instant finishedAtSafe(FocusSession s) {
        // assumes finished session has end != null
        return s.getTimeRange().getEnd();
    }

    @Override
    public void save(FocusSession session) {
        store.put(session.getId(), session);
    }

    @Override
    public boolean existsByOwnerAndCategoryId(UserId ownerId, CategoryId categoryId) {
        return store.values().stream()
                .filter(s -> s.getOwner().equals(ownerId))
                .anyMatch(s -> categoryId.equals(s.getCategoryId()));
    }

    // Optional helper for tests
    public void clear() {
        store.clear();
    }
}
