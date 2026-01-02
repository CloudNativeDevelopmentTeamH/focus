package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.FocusSessionId;

import java.time.Duration;
import java.time.Instant;

/**
 * Duration may be null if the session is still running
 */
public record SessionView(
        FocusSessionId id,
        Instant startAt,
        Instant endAt,
        Duration duration,
        CategoryId categoryId,
        String note,
        boolean running
) {}
