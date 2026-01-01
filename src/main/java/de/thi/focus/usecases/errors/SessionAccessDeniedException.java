package de.thi.focus.usecases.errors;

import de.thi.focus.entities.ids.FocusSessionId;
import de.thi.focus.entities.ids.UserId;

public final class SessionAccessDeniedException extends RuntimeException {

    private final UserId userId;
    private final FocusSessionId sessionId;

    public SessionAccessDeniedException(UserId userId, FocusSessionId sessionId) {
        super("User is not allowed to access this focus session (userId=" + userId + ", sessionId=" + sessionId + ").");
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public UserId getUserId() {
        return userId;
    }

    public FocusSessionId getSessionId() {
        return sessionId;
    }
}
