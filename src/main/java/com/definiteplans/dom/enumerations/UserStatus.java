package com.definiteplans.dom.enumerations;

import java.util.List;

public enum UserStatus implements GenericEnum {
    STUB_ACCOUNT(1, "Stub"),
    PENDING_EMAIL_VALIDATION(2, "Pending"),
    ACTIVE(3, "Active"),
    DISABLED(4, "Disabled");

    public static final List<UserStatus> VALUES = List.of(UserStatus.values());

    private final int id;
    private String description;

    UserStatus(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static UserStatus getById(int id) {
        return VALUES.stream().filter(dt -> dt.id == id).findFirst().orElse(null);
    }

    @Override public void setDescription(String description) { this.description = description; }
    @Override public String getDescription() { return description; }
    @Override public int getId() { return id; }
}
