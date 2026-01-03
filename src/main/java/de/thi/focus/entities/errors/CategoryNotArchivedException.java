package de.thi.focus.entities.errors;

import de.thi.focus.entities.ids.CategoryId;

public final class CategoryNotArchivedException extends DomainException {
    public static final String CODE = "CATEGORY_NOT_ARCHIVED";

    private final CategoryId categoryId;

    public CategoryNotArchivedException(CategoryId categoryId) {
        super(CODE, "Category is not archived (categoryId=" + categoryId + ").");
        this.categoryId = categoryId;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }
}
