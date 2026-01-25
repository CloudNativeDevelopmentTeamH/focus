package de.thi.focus.entities.ids;

import java.util.Objects;

public final class UserId {
    private final int value;

    public UserId(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value must be a positive integer");
        }
        this.value = value;
    }

    public static UserId fromString(String raw) {
        Objects.requireNonNull(raw, "raw must not be null");

        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("raw must not be blank");
        }

        try {
            return new UserId(Integer.parseInt(trimmed));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("raw must be a valid integer user id, was: " + raw, e);
        }
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId)) return false;
        UserId userId = (UserId) o;
        return value == userId.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
