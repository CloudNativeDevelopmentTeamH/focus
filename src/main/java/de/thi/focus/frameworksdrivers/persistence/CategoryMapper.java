package de.thi.focus.frameworksdrivers.persistence;

import de.thi.focus.entities.Category;
import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.valueobjects.CategoryName;
import de.thi.focus.entities.valueobjects.Color;
import de.thi.focus.frameworksdrivers.persistence.jpa.CategoryEntity;
import de.thi.focus.usecases.factories.FocusValueObjectFactory;

final class CategoryMapper {

    static CategoryEntity toEntity(Category domain) {
        var e = new CategoryEntity();
        e.id = domain.getId().value();
        e.ownerId = domain.getOwner().value();
        e.name = domain.getName().value();
        e.color = domain.getColor().value();
        e.archived = domain.isArchived();
        return e;
    }

    static Category toDomain(CategoryEntity e, FocusValueObjectFactory voFactory) {
        CategoryName name = voFactory.categoryName(e.name);
        Color color = voFactory.colorOrDefault(e.color);

        return Category.rehydrate(
                new CategoryId(e.id),
                new UserId(e.ownerId),
                name,
                color,
                e.archived
        );
    }

    static String categoryNameToString(CategoryName name) {
        return name.value();
    }
}
