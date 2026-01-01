package de.thi.focus.entities.errors;

import de.thi.focus.entities.ids.FocusSessionId;

public final class SessionStillRunningException extends DomainException {
    public static final String CODE = "SESSION_STILL_RUNNING";

    private final FocusSessionId sessionId;

    public SessionStillRunningException(FocusSessionId sessionId) {
        super(CODE, buildMessage(sessionId));
        this.sessionId = sessionId;
    }

    public FocusSessionId getSessionId() {
        return sessionId;
    }

    private static String buildMessage(FocusSessionId sessionId) {
        return "Cannot compute duration for a running focus session (sessionId=" + sessionId + ").";
    }
}
