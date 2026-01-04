package de.thi.focus.frameworksdrivers.persistence;

import de.thi.focus.entities.FocusSession;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.valueobjects.Note;
import de.thi.focus.entities.valueobjects.TimeRange;
import de.thi.focus.frameworksdrivers.persistence.jpa.FocusSessionEntity;
import de.thi.focus.usecases.factories.FocusValueObjectFactory;

import java.time.Instant;

final class FocusSessionMapper {

    static FocusSessionEntity toEntity(FocusSession domain) {
        var e = new FocusSessionEntity();

        e.id = domain.getId().value();
        e.ownerId = domain.getOwner().value();

        e.startAt = domain.getTimeRange().getStart();
        e.endAt = domain.getTimeRange().getEnd(); // null if running

        e.categoryId = domain.getCategoryId() != null
                ? domain.getCategoryId().value()
                : null;

        e.note = domain.getNote() != null
                ? noteToString(domain.getNote())
                : null;

        return e;
    }

    static FocusSession toDomain(FocusSessionEntity e, FocusValueObjectFactory voFactory) {
        TimeRange timeRange = rehydrateTimeRange(e.startAt, e.endAt);

        return FocusSession.reconstitute(
                new FocusSessionId(e.id),
                new UserId(e.ownerId),
                timeRange,
                e.categoryId != null ? new CategoryId(e.categoryId) : null,
                e.note != null ? voFactory.note(e.note) : null
        );
    }


    private static TimeRange rehydrateTimeRange(Instant start, Instant end) {
        TimeRange tr = TimeRange.startingAt(start);
        return end != null ? tr.stopAt(end) : tr;
    }

    private static String noteToString(Note note) {
        return note.toString();
    }
}
