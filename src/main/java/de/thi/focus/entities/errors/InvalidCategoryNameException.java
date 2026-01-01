package de.thi.focus.entities.errors;

public class InvalidCategoryNameException extends DomainException {
    public static final String CODE = "INVALID_CATEGORY_NAME";

    private final String rawName;
    private final int maxLength;

    public InvalidCategoryNameException(String rawName, int maxLength, String reason) {
        super(CODE, buildMessage(rawName, maxLength, reason));
        this.rawName = rawName;
        this.maxLength = maxLength;
    }

    public String getRawName() {
        return rawName;
    }

    public int getMaxLength() {
        return maxLength;
    }

    private static String buildMessage(String rawName, int maxLength, String reason) {
        return "Invalid category name (rawName=" + rawName + ", maxLength=" + maxLength + "): " + reason;
    }
}
