package de.thi.focus.usecases.errors;

import de.thi.focus.entities.ids.UserId;

public final class NoPreviousSessionToResumeException extends RuntimeException {

    private final UserId userId;

    public NoPreviousSessionToResumeException(UserId userId) {
        super("No finished focus session found to resume for userId=" + userId);
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
