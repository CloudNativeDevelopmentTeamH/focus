package de.thi.focus.entities.events;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

public final class CategoryArchived implements DomainEvent {

    private final CategoryId categoryId;
    private final UserId userId;
    private final boolean archived;
    private final Instant occurredAt;

    public CategoryArchived(CategoryId categoryId, UserId userId, boolean archived, Instant occurredAt) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.archived = archived;
        this.occurredAt = occurredAt;
    }

    public CategoryId categoryId() {
        return categoryId;
    }

    public UserId userId() {
        return userId;
    }

    public boolean archived() {
        return archived;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}
