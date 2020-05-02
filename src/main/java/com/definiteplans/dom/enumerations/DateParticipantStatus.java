package com.definiteplans.dom.enumerations;

import java.util.List;

public enum DateParticipantStatus implements GenericEnum {
    WAITING_FOR_REPLY(1, "Waiting for Reply"),
    NEEDS_TO_REPLY(2, "Needs to Reply"),
    ACCEPTED(3, "Accepted"),
    DECLINED(4, "Declined");

    public static final List<DateParticipantStatus> VALUES = List.of(DateParticipantStatus.values());

    private final int id;
    private String description;

    DateParticipantStatus(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static DateParticipantStatus getById(int id) {
        return VALUES.stream().filter(dt -> dt.id == id).findFirst().orElse(null);
    }

    @Override public void setDescription(String description) { this.description = description; }
    @Override public String getDescription() { return description; }
    @Override public int getId() { return id; }
}
