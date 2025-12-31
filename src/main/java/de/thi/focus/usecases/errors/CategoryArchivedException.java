package de.thi.focus.usecases.errors;

import de.thi.focus.entities.ids.CategoryId;

public final class CategoryArchivedException extends RuntimeException {

    private final CategoryId categoryId;

    public CategoryArchivedException(CategoryId categoryId) {
        super("Category is archived (categoryId=" + categoryId + ").");
        this.categoryId = categoryId;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }
}
