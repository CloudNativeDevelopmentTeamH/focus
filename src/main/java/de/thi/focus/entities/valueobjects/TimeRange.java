package de.thi.focus.entities.valueobjects;

import de.thi.focus.entities.errors.InvalidTimeRangeException;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class TimeRange {
    private final Instant start;
    private final Instant end; // nullable

    private TimeRange(Instant start, Instant end) {
        this.start = Objects.requireNonNull(start, "start must not be null");
        this.end = end;

        if (end != null && !end.isAfter(start)) {
            throw new InvalidTimeRangeException(start, end);
        }
    }

    public static TimeRange startingAt(Instant start) {
        return new TimeRange(start, null);
    }

    public static TimeRange between(Instant start, Instant end) {
        return new TimeRange(start, end);
    }

    public boolean isRunning() {
        return end == null;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public TimeRange stopAt(Instant endedAt) {
        if (this.end != null) {
            throw new InvalidTimeRangeException(start, endedAt);
        }
        return new TimeRange(this.start, endedAt);
    }

    public Duration duration() {
        if (end == null) {
            throw new InvalidTimeRangeException(start, null);
        }
        return Duration.between(start, end);
    }
}
