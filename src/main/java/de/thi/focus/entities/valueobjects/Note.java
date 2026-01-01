package de.thi.focus.entities.valueobjects;

import de.thi.focus.entities.errors.NoteTooLongException;

import java.util.Objects;

public final class Note {
    public static final int MAX_LENGTH = 1000; //TODO: configurable via env

    private final String value;

    public Note(String raw) {
        if (raw == null) {
            this.value = "";
            return;
        }

        String normalized = raw.trim();

        if (normalized.length() > MAX_LENGTH) {
            throw new NoteTooLongException(normalized.length(), MAX_LENGTH);
        }
        this.value = normalized;
    }

    public String value() {
        return value;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note that = (Note) o;
        return Objects.equals(value, that.value);
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
