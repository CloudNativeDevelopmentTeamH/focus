package de.thi.focus.entities;

import de.thi.focus.entities.errors.SessionAlreadyStoppedException;
import de.thi.focus.entities.errors.SessionAlreadyStoppedException;
import de.thi.focus.entities.errors.SessionStillRunningException;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.valueobjects.TimeRange;
import de.thi.focus.entities.valueobjects.Note;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class FocusSession {
    private final FocusSessionId id;
    private final UserId owner;
    private TimeRange timeRange;
    private CategoryId categoryId; // nullable
    private Note note; // nullable

    private FocusSession(FocusSessionId id, UserId owner, TimeRange timeRange, CategoryId categoryId, Note note) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.owner = Objects.requireNonNull(owner, "owner must not be null");
        this.timeRange = Objects.requireNonNull(timeRange, "timeRange must not be null");
        this.categoryId = categoryId;
        this.note = note;
    }

    public static FocusSession start(FocusSessionId id, UserId owner, Instant startedAt, CategoryId categoryId, Note note) {
        return new FocusSession(id, owner, TimeRange.startingAt(startedAt), categoryId, note);
    }

    // Rehydration from persistence - no invariant checks, data is already validated
    public static FocusSession reconstitute(FocusSessionId id, UserId owner, TimeRange timeRange, CategoryId categoryId, Note note) {
        return new FocusSession(id, owner, timeRange, categoryId, note);
    }

    public FocusSessionId getId() {
        return id;
    }

    public UserId getOwner() {
        return owner;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public boolean isRunning() {
        return timeRange.isRunning();
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public Note getNote() {
        return note;
    }

    public void stopAt(Instant endedAt) {
        if(!timeRange.isRunning()) {
            throw new SessionAlreadyStoppedException(id, timeRange.getEnd());
        }
        this.timeRange = this.timeRange.stopAt(endedAt);
    }

    public Duration duration() {
        if(timeRange.isRunning()) {
            throw new SessionStillRunningException(id);
        }
        return timeRange.duration();
    }

    public void clearCategory() {
        this.categoryId = null;
    }

    public void changeCategory(CategoryId newCategoryId) {
        this.categoryId = Objects.requireNonNull(newCategoryId, "newCategoryId must not be null");
    }

    public void updateNote(Note newNote) {
        this.note = Objects.requireNonNull(newNote, "newNote must not be null");
    }

    public void clearNote() {
        this.note = null;
    }
}
