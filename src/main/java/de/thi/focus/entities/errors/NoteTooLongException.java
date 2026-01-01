package de.thi.focus.entities.errors;

public final class NoteTooLongException extends DomainException {
    public static final String CODE = "NOTE_TOO_LONG";

    private final int actualLength;
    private final int maxLength;

    public NoteTooLongException(int actualLength, int maxLength) {
        super(CODE, buildMessage(actualLength, maxLength));
        this.actualLength = actualLength;
        this.maxLength = maxLength;
    }

    public int getActualLength() {
        return actualLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    private static String buildMessage(int actualLength, int maxLength) {
        return "Note exceeds maximum length (actual=" + actualLength + ", max=" + maxLength + ").";
    }
}
