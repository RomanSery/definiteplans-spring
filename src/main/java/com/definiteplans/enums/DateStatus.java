package com.definiteplans.enums;

public enum DateStatus {
    NEGOTIATION(1), APPROVED(2), OCCURED(3), DELETED(4);
    private int code;

    DateStatus(int c) {
        this.code = c;
    }

    public static DateStatus fromInt(int x) {
        switch (x) {
            case 1:
                return NEGOTIATION;
            case 2:
                return APPROVED;
            case 3:
                return OCCURED;
            case 4:
                return DELETED;
        }

        return NEGOTIATION;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int c) {
        this.code = c;
    }
}
