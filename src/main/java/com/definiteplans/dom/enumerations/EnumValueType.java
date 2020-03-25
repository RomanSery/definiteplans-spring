package com.definiteplans.dom.enumerations;

import java.util.List;

public enum EnumValueType implements GenericEnum {
    LANGUAGE(1, "Language"),
    ETHNICITY(3, "Ethnicity"),
    SMOKES(4, "Smokes"),
    RELIGION(5, "Religion"),
    EDUCATION(6, "Education"),
    INCOME(7, "Income"),
    KIDS(8, "Kids"),
    WANTS_KIDS(9, "Wants Kids"),
    MARITAL_STATUS(10, "Marital Status"),
    GENDER(11, "Gender");

    public static final List<EnumValueType> VALUES = List.of(EnumValueType.values());

    private final int id;
    private String description;

    EnumValueType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static EnumValueType getById(int id) {
        return VALUES.stream().filter(dt -> dt.id == id).findFirst().orElse(null);
    }

    @Override public void setDescription(String description) { this.description = description; }
    @Override public String getDescription() { return description; }
    @Override public int getId() { return id; }
}
