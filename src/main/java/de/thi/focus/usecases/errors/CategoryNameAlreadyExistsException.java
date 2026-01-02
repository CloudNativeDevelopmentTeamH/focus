package de.thi.focus.usecases.errors;

import de.thi.focus.entities.valueobjects.CategoryName;

public final class CategoryNameAlreadyExistsException extends RuntimeException {

    private final CategoryName name;

    public CategoryNameAlreadyExistsException(CategoryName name) {
        super("Category name already exists: " + name.value());
        this.name = name;
    }

    public CategoryName getName() {
        return name;
    }
}
