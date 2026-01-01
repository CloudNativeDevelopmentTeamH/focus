package de.thi.focus.usecases.errors;

import de.thi.focus.entities.ids.UserId;

public final class RunningSessionAlreadyExistsException extends RuntimeException {

    private final UserId userId;

    public RunningSessionAlreadyExistsException(UserId userId) {
        super("User already has a running focus session (userId=" + userId + ").");
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
