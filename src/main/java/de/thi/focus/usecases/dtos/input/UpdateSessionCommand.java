package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

/**
 * Fields may be null to indicate "no change"
 * To clear category/note, pass the respective Clear flag (avoids ambiguous null)
 */
public record UpdateSessionCommand(
        UserId userId,
        FocusSessionId sessionId,
        Instant startAt,
        Instant endAt,
        CategoryId categoryId,
        Boolean clearCategory,
        String note,
        Boolean clearNote
) {}
