package de.thi.focus.usecases.errors;

import de.thi.focus.entities.ids.CategoryId;

public final class CategoryInUseException extends RuntimeException {

    private final CategoryId categoryId;

    public CategoryInUseException(CategoryId categoryId) {
        super("Category is in use and cannot be deleted (categoryId=" + categoryId + ").");
        this.categoryId = categoryId;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }
}
