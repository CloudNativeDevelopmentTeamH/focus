package de.thi.focus.entities.valueobjects;

import de.thi.focus.entities.errors.InvalidCategoryNameException;

import java.util.Objects;

public final class CategoryName {

    private final String value;

    private CategoryName(String value) {
        this.value = value;
    }

    public static CategoryName of(String raw, int maxLength) {
        if (raw == null) {
            throw new InvalidCategoryNameException(null, maxLength, "name must not be null");
        }

        String normalized = raw.trim();

        if (normalized.isEmpty()) {
            throw new InvalidCategoryNameException(raw, maxLength, "name must not be empty");
        }

        if (normalized.length() > maxLength) {
            throw new InvalidCategoryNameException(
                    raw,
                    maxLength,
                    "name must not exceed " + maxLength + " characters"
            );
        }

        return new CategoryName(normalized);
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
