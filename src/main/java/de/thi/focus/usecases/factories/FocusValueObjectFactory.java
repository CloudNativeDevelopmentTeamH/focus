package de.thi.focus.usecases.factories;

import de.thi.focus.config.FocusConstraintsConfig;
import de.thi.focus.config.FocusDefaultsConfig;
import de.thi.focus.entities.valueobjects.CategoryName;
import de.thi.focus.entities.valueobjects.Color;
import de.thi.focus.entities.valueobjects.Note;

import java.util.Objects;

public final class FocusValueObjectFactory {

    private final FocusConstraintsConfig constraints;
    private final FocusDefaultsConfig defaults;

    public FocusValueObjectFactory(FocusConstraintsConfig constraints, FocusDefaultsConfig defaults) {
        this.constraints = Objects.requireNonNull(constraints);
        this.defaults = Objects.requireNonNull(defaults);
    }

    public Note note(String raw) {
        return Note.of(raw, constraints.note().maxLength());
    }

    public CategoryName categoryName(String raw) {
        return CategoryName.of(raw, constraints.category().name().maxLength());
    }

    public Color colorOrDefault(String raw) {
        if (raw == null || raw.isBlank()) {
            return new Color(defaults.categoryColor());
        }
        return new Color(raw);
    }

    public Color colorRequired(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("color must not be null or blank");
        }
        return new Color(raw);
    }
}
