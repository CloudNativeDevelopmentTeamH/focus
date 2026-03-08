package de.thi.focus.entities.events;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;

import java.time.Duration;
import java.time.Instant;

public class FocusSessionStopped implements DomainEvent {
    private final FocusSessionId sessionId;
    private final UserId userId;
    private final CategoryId categoryId;
    private final Instant endedAt;
    private final Duration duration;
    private final Instant occurredAt;

    public FocusSessionStopped(
            FocusSessionId sessionId,
            UserId userId,
            CategoryId categoryId,
            Instant endedAt,
            Duration duration,
            Instant occurredAt
    ) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.endedAt = endedAt;
        this.duration = duration;
        this.occurredAt = occurredAt;
    }

    public FocusSessionId sessionId() {
        return sessionId;
    }

    public UserId userId() {
        return userId;
    }

    public CategoryId categoryId() {
        return categoryId;
    }

    public Instant endedAt() {
        return endedAt;
    }

    public Duration duration() {
        return duration;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}
