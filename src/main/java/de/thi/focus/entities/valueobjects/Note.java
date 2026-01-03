package de.thi.focus.entities.valueobjects;

import de.thi.focus.entities.errors.NoteTooLongException;

import java.util.Objects;

public final class Note {

    private final String value;

    private Note(String normalized) {
        this.value = normalized;
    }

    public static Note of(String raw, int maxLength) {
        String normalized = normalize(raw);

        if (normalized.length() > maxLength) {
            throw new NoteTooLongException(normalized.length(), maxLength);
        }
        return new Note(normalized);
    }

    public static Note empty() {
        return new Note("");
    }

    private static String normalize(String raw) {
        if (raw == null) return "";
        return raw.trim();
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
