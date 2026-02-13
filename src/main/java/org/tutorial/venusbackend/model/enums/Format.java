package org.tutorial.venusbackend.model.enums;

public enum Format {
    HARDCOVER("Hardcover"),
    PAPERBACK("Paperback");

    private final String displayName;

    Format(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
