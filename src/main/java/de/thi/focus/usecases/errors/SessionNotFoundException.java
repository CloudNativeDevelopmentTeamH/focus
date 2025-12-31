package de.thi.focus.usecases.errors;

import de.thi.focus.entities.ids.FocusSessionId;

public final class SessionNotFoundException extends RuntimeException {

    private final FocusSessionId sessionId;

    public SessionNotFoundException(FocusSessionId sessionId) {
        super("Focus session not found (sessionId=" + sessionId + ").");
        this.sessionId = sessionId;
    }

    public FocusSessionId getSessionId() {
        return sessionId;
    }
}
