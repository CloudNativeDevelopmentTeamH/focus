package de.thi.focus.usecases.policies;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.valueobjects.CategoryName;
import de.thi.focus.usecases.errors.CategoryNameAlreadyExistsException;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;

import java.util.Objects;
import java.util.Optional;

public final class UniqueCategoryNamePolicy {

    private final CategoryRepository categoryRepository;

    public UniqueCategoryNamePolicy(CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
    }

    /**
     * Ensures that the given name does not exist for the user.
     * currentCategoryId is used to allow renaming a category to its own current name.
     */
    public void ensureUnique(UserId userId, CategoryName name, CategoryId currentCategoryId) {
        Optional<Category> existing = categoryRepository.findByOwnerAndName(userId, name);

        if (existing.isEmpty()) return;

        if (!existing.get().getId().equals(currentCategoryId)) {
            throw new CategoryNameAlreadyExistsException(name);
        }
    }
}
