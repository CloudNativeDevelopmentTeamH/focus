package de.thi.focus.frameworksdrivers.persistence;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.valueobjects.CategoryName;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryCategoryRepository implements CategoryRepository {

    private final ConcurrentHashMap<CategoryId, Category> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Category> findById(CategoryId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Category category) {
        store.put(category.getId(), category);
    }

    @Override
    public Optional<Category> findByOwnerAndName(UserId ownerId, CategoryName name) {
        return store.values().stream()
                .filter(c -> c.getOwner().equals(ownerId))
                .filter(c -> c.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Category> findAllByOwner(UserId userId) {
        return store.values().stream()
                .filter(c -> c.getOwner().equals(userId))
                .toList();
    }

    // Optional helper
    public void clear() {
        store.clear();
    }
}
