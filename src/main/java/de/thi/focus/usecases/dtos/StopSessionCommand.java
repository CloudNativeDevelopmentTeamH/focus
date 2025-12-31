package de.thi.focus.usecases.dtos;

import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;

import java.time.Instant;

/**
 * endedAt may be null -> the use case may use Clock.now()
 */
public record StopSessionCommand(
        UserId userId,
        FocusSessionId sessionId,
        Instant endedAt
) {}
