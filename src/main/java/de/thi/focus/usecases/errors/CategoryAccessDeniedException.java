package de.thi.focus.usecases.errors;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

public final class CategoryAccessDeniedException extends RuntimeException {

    private final UserId userId;
    private final CategoryId categoryId;

    public CategoryAccessDeniedException(UserId userId, CategoryId categoryId) {
        super("User is not allowed to use this category (userId=" + userId + ", categoryId=" + categoryId + ").");
        this.userId = userId;
        this.categoryId = categoryId;
    }

    public UserId getUserId() {
        return userId;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }
}
