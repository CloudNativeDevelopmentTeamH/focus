package de.thi.focus.entities.valueobjects;

import de.thi.focus.entities.errors.InvalidColorException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Color {
    private static final Pattern HEX_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6})$");

    private final String value;

    public Color(String raw) {
        if (raw == null) {
            throw new InvalidColorException(null, "color must not be null");
        }

        String normalized = raw.trim();

        if (!HEX_PATTERN.matcher(normalized).matches()) {
            throw new InvalidColorException(raw, "color must be in hex format #RRGGBB");
        }

        this.value = normalized.toUpperCase();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color)) return false;
        Color that = (Color) o;
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
