package de.thi.focus.interfaceadapters.web.security;

import de.thi.focus.entities.ids.UserId;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class CurrentUser {
    private UserId userId;

    public UserId userId() {
        if (userId == null) throw new IllegalStateException("User not resolved");
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }
}
