package com.coderdreams.definiteplans.enums;

public enum UserStatus {
    STUB_ACCOUNT(1), PENDING_EMAIL_VALIDATION(2), ACTIVE(3), DISABLED(4);
    private int code;

    UserStatus(int c) {
        this.code = c;
    }

    public static UserStatus fromInt(int x) {
        switch (x) {
            case 1:
                return STUB_ACCOUNT;
            case 2:
                return PENDING_EMAIL_VALIDATION;
            case 3:
                return ACTIVE;
            case 4:
                return DISABLED;
        }

        return STUB_ACCOUNT;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int c) {
        this.code = c;
    }
}
