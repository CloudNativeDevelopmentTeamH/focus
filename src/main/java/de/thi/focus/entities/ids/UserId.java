package de.thi.focus.entities.ids;

import java.util.Objects;
import java.util.UUID;

public class UserId {
    private final UUID value;

    public UserId(UUID value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    public static UserId fromString(String raw) {
        Objects.requireNonNull(raw, "raw must not be null");
        return new UserId(UUID.fromString(raw.trim()));
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId)) return false;
        UserId userId = (UserId) o;
        return value.equals(userId.value);
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
