package org.tutorial.venusbackend.model.enums;

public enum Language {
    SWEDISH("Swedish"),
    ENGLISH("English");

    private final String displayName;

    Language(String displayName) {
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