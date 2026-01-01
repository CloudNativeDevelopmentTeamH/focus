package de.thi.focus.entities.errors;

public class InvalidColorException extends DomainException {
    public static final String CODE = "INVALID_COLOR";

    private final String rawColor;

    public InvalidColorException(String rawColor, String reason) {
        super(CODE, buildMessage(rawColor, reason));
        this.rawColor = rawColor;
    }

    public String getRawColor() {
        return rawColor;
    }

    private static String buildMessage(String rawColor, String reason) {
        return "Invalid color value (rawColor=" + rawColor + "): " + reason;
    }
}
