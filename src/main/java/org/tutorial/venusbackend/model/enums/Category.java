package org.tutorial.venusbackend.model.enums;

public enum Category {
    FANTASY("Fantasy"),
    ROMANCE("Romance"),
    FICTION("Fiction"),
    CRIME("Crime"),
    SCIENCE("Science"),
    BIOGRAPHY("Biography"),
    HISTORY("History"),
    CHILDREN_AND_TEENS("Children & Teens"),
    HORROR("Horror"),
    ADVENTURE("Adventure");

    private final String displayName;

    Category(String displayName) {
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