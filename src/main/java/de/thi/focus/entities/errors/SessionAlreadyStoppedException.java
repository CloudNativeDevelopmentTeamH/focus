package de.thi.focus.entities.errors;

import de.thi.focus.entities.ids.FocusSessionId;

import java.time.Instant;

public final class SessionAlreadyStoppedException extends DomainException {
    public static final String CODE = "SESSION_ALREADY_STOPPED";

    private final FocusSessionId sessionId;
    private final Instant endedAt;

    public SessionAlreadyStoppedException(FocusSessionId sessionId, Instant endedAt) {
        super(CODE, buildMessage(sessionId, endedAt));
        this.sessionId = sessionId;
        this.endedAt = endedAt;
    }

    public FocusSessionId getSessionId() {
        return sessionId;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    private static String buildMessage(FocusSessionId sessionId, Instant endedAt) {
        return "Focus session is already stopped (sessionId=" + sessionId + ", endedAt=" + endedAt + ").";
    }
}
