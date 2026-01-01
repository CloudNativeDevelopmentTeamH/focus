package de.thi.focus.entities.events;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

public class FocusSessionStarted implements DomainEvent {
    private final FocusSessionId sessionId;
    private final UserId userId;
    private final Instant startedAt;
    private final CategoryId categoryId; // nullable
    private final Instant occurredAt;

    public FocusSessionStarted(FocusSessionId sessionId, UserId userId, Instant startedAt, CategoryId categoryId, Instant occurredAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.startedAt = startedAt;
        this.categoryId = categoryId;
        this.occurredAt = occurredAt;
    }

    public FocusSessionId sessionId() {
        return sessionId;
    }

    public UserId userId() {
        return userId;
    }

    public Instant startedAt() {
        return startedAt;
    }

    public CategoryId categoryId() {
        return categoryId;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}
