package com.definiteplans.dom.enumerations;

import java.util.List;

public enum DateStatus implements GenericEnum {
    NEGOTIATION(1, "Negotiation"),
    APPROVED(2, "Approved"),
    OCCURED(3, "Occured"),
    DELETED(4, "Deleted");

    public static final List<DateStatus> VALUES = List.of(DateStatus.values());

    private final int id;
    private String description;

    DateStatus(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static DateStatus getById(int id) {
        return VALUES.stream().filter(dt -> dt.id == id).findFirst().orElse(null);
    }

    @Override public void setDescription(String description) { this.description = description; }
    @Override public String getDescription() { return description; }
    @Override public int getId() { return id; }
}
