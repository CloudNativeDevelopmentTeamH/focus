package de.thi.focus.entities.ids;

import java.util.Objects;
import java.util.UUID;

public class CategoryId {
    private final UUID value;

    public CategoryId(UUID value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }

    public static CategoryId fromString(String raw) {
        Objects.requireNonNull(raw, "raw must not be null");
        return new CategoryId(UUID.fromString(raw.trim()));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryId)) return false;
        CategoryId that = (CategoryId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
