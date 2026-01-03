package de.thi.focus.usecases.ports.outbound;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.valueobjects.CategoryName;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(CategoryId id);

    void save(Category category);

    Optional<Category> findByOwnerAndName(UserId ownerId, CategoryName name);

    List<Category> findAllByOwner(UserId userId);

    void deleteById(CategoryId id);
}

