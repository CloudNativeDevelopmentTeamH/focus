package de.thi.focus.entities;

import de.thi.focus.entities.ids.CategoryId;
import de.thi.focus.entities.ids.UserId;
import de.thi.focus.entities.valueobjects.CategoryName;
import de.thi.focus.entities.valueobjects.Color;

import java.util.Objects;

public class Category {
    private final CategoryId id;
    private final UserId owner;

    private CategoryName name;
    private Color color;
    private boolean archived;

    private Category(CategoryId id, UserId owner, CategoryName name, Color color, boolean archived) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.owner = Objects.requireNonNull(owner, "owner must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.color = Objects.requireNonNull(color, "color must not be null");
        this.archived = archived;
    }

    /* --- Factory --- */
    public static Category create(CategoryId id, UserId owner, CategoryName name, Color color) {
        return new Category(id, owner, name, color, false);
    }

    /* --- Getters --- */
    public CategoryId getId() {
        return id;
    }
    public UserId getOwner() {
        return owner;
    }

    public CategoryName getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public boolean isArchived() {
        return archived;
    }

    /* --- Domain Behavior --- */
    public void rename(CategoryName newName) {
        this.name = Objects.requireNonNull(newName, "newName must not be null");
    }

    public void recolor(Color newColor) {
        this.color = Objects.requireNonNull(newColor, "newColor must not be null");
    }

    public void archive() {
        this.archived = true;
    }

    public void unarchive() {
        this.archived = false;
    }
}
