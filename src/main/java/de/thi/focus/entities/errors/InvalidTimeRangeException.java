package de.thi.focus.entities.errors;

import java.time.Instant;

public class InvalidTimeRangeException extends DomainException {
    public static final String CODE = "INVALID_TIME_RANGE";

    private final Instant start;
    private final Instant end;

    public InvalidTimeRangeException(Instant start, Instant end) {
        super(CODE, buildMessage(start, end));
        this.start = start;
        this.end = end;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    private static String buildMessage(Instant start, Instant end) {
        if (end == null) {
            return "Invalid time range: end must be provided and strictly after start (start=" + start + ", end=null).";
        }
        return "Invalid time range: end must be strictly after start (start=" + start + ", end=" + end + ").";
    }
}
