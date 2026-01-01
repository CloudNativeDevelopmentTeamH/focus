package de.thi.focus.usecases.ports;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;

import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(CategoryId id);

    default Optional<Category> findByIdForUser(CategoryId id, UserId userId) {
        return findById(id).filter(c -> c.getOwner().equals(userId));
    }
}
