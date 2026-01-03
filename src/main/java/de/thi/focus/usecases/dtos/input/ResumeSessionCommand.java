package de.thi.focus.usecases.dtos.input;

import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

public record ResumeSessionCommand(
        UserId userId,
        FocusSessionId previousSessionId,
        Instant startedAt
) {}
