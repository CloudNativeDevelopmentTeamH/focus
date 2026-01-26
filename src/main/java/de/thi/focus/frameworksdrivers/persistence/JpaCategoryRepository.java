package de.thi.focus.frameworksdrivers.persistence;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.valueobjects.CategoryName;
import de.thi.focus.frameworksdrivers.persistence.jpa.CategoryEntity;
import de.thi.focus.usecases.factories.FocusValueObjectFactory;
import de.thi.focus.usecases.ports.outbound.CategoryRepository;
import jakarta.enterprise.inject.Vetoed;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Vetoed
public class JpaCategoryRepository implements CategoryRepository {

    private final EntityManager em;
    private final FocusValueObjectFactory voFactory;

    public JpaCategoryRepository(EntityManager em, FocusValueObjectFactory voFactory) {
        this.em = em;
        this.voFactory = voFactory;
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        var e = em.find(CategoryEntity.class, id.value());
        return Optional.ofNullable(e).map(ent -> CategoryMapper.toDomain(ent, voFactory));
    }

    @Override
    public Optional<Category> findByOwnerAndName(UserId ownerId, CategoryName name) {
        var list = em.createQuery("""
                select c from CategoryEntity c
                where c.ownerId = :ownerId and c.name = :name
                """, CategoryEntity.class)
                .setParameter("ownerId", ownerId.value())
                .setParameter("name", CategoryMapper.categoryNameToString(name))
                .setMaxResults(1)
                .getResultList();

        return list.stream().findFirst().map(ent -> CategoryMapper.toDomain(ent, voFactory));
    }

    @Override
    public List<Category> findAllByOwner(UserId userId) {
        return em.createQuery("""
                select c from CategoryEntity c
                where c.ownerId = :ownerId
                order by c.name asc
                """, CategoryEntity.class)
                .setParameter("ownerId", userId.value())
                .getResultList()
                .stream()
                .map(ent -> CategoryMapper.toDomain(ent, voFactory))
                .toList();
    }

    @Override
    @Transactional
    public void save(Category category) {
        CategoryEntity existing = em.find(CategoryEntity.class, category.getId().value());
        
        if (existing == null) {
            // New entity
            em.persist(CategoryMapper.toEntity(category));
        } else {
            // Update existing entity
            existing.ownerId = category.getOwner().value();
            existing.name = category.getName().value();
            existing.color = category.getColor().value();
            existing.archived = category.isArchived();
            // createdAt and updatedAt are managed by @PrePersist/@PreUpdate
        }
    }

    @Override
    @Transactional
    public void deleteById(CategoryId id) {
        var e = em.find(CategoryEntity.class, id.value());
        if (e != null) em.remove(e);
    }
}
