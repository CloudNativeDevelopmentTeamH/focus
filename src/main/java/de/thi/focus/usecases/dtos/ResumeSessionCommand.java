package de.thi.focus.usecases.dtos;

import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

public record ResumeSessionCommand(
        UserId userId,
        Instant startedAt
) {}
