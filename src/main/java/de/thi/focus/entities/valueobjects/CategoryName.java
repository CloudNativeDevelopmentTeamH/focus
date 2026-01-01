package de.thi.focus.entities.valueobjects;

import de.thi.focus.entities.errors.InvalidCategoryNameException;

import java.util.Objects;

public class CategoryName {
    public static final int MAX_LENGTH = 50;

    private final String value;

    public CategoryName(String raw) {
        if (raw == null) {
            throw new InvalidCategoryNameException(null, MAX_LENGTH, "name must not be null");
        }

        String normalized = raw.trim();

        if (normalized.isEmpty()) {
            throw new InvalidCategoryNameException(raw, MAX_LENGTH, "name must not be empty");
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new InvalidCategoryNameException(raw, MAX_LENGTH, "name must not exceed " + MAX_LENGTH + " characters");
        }

        this.value = normalized;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryName)) return false;
        CategoryName that = (CategoryName) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
