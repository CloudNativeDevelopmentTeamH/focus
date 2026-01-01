package de.thi.focus.entities.ids;

import java.util.Objects;
import java.util.UUID;

public class FocusSessionId {
    private final UUID value;

    public FocusSessionId(UUID value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    public static FocusSessionId newId() {
        return new FocusSessionId(UUID.randomUUID());
    }

    public static FocusSessionId fromString(String raw) {
        Objects.requireNonNull(raw, "raw must not be null");
        return new FocusSessionId(UUID.fromString(raw.trim()));
    }

    public UUID value() {
        return value;
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
