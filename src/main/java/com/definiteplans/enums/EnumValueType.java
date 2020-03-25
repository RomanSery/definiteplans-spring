package com.definiteplans.enums;

public enum EnumValueType {
    LANGUAGE(1), ETHNICITY(3), SMOKES(4), RELIGION(5), EDUCATION(6), INCOME(7), KIDS(8), WANTS_KIDS(9), MARITAL_STATUS(10), GENDER(11);
    private int code;

    EnumValueType(int c) {
        this.code = c;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int c) {
        this.code = c;
    }
}
