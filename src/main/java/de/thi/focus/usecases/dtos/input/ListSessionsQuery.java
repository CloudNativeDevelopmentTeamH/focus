package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

public record ListSessionsQuery(
        UserId userId,
        Instant from,
        Instant to,
        CategoryId categoryId,
        Boolean runningOnly
) {}
