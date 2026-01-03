package de.thi.focus.entities.errors;

import de.thi.focus.entities.ids.CategoryId;

public final class CategoryAlreadyArchivedException extends DomainException {
    public static final String CODE = "CATEGORY_ALREADY_ARCHIVED";

    private final CategoryId categoryId;

    public CategoryAlreadyArchivedException(CategoryId categoryId) {
        super(CODE, "Category is already archived (categoryId=" + categoryId + ").");
        this.categoryId = categoryId;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }
}
