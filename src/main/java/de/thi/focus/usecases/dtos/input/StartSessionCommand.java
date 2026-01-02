package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

/**
 * startedAt may be null -> the use case may use Clock.now()
 * categoryId / note are optional
 */
public record StartSessionCommand(
        UserId userId,
        Instant startedAt,
        CategoryId categoryId,
        String note
) {}
