package de.thi.focus.usecases.errors;

import de.thi.focus.entities.ids.CategoryId;

public final class CategoryNotFoundException extends RuntimeException {

    private final CategoryId categoryId;

    public CategoryNotFoundException(CategoryId categoryId) {
        super("Category not found (categoryId=" + categoryId + ").");
        this.categoryId = categoryId;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }
}
