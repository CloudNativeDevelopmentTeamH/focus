package de.thi.focus.entities.events;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

public final class CategoryCreated implements DomainEvent {

    private final CategoryId categoryId;
    private final UserId userId;
    private final Instant occurredAt;

    public CategoryCreated(CategoryId categoryId, UserId userId, Instant occurredAt) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.occurredAt = occurredAt;
    }

    public CategoryId categoryId() {
        return categoryId;
    }

    public UserId userId() {
        return userId;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}
