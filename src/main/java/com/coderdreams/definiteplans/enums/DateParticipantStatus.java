package com.coderdreams.definiteplans.enums;

public enum DateParticipantStatus {
    WAITING_FOR_REPLY(1), NEEDS_TO_REPLY(2), APPROVED(3), DECLINED(4);
    private int code;

    DateParticipantStatus(int c) {
        this.code = c;
    }

    public static DateParticipantStatus fromInt(int x) {
        switch (x) {
            case 1:
                return WAITING_FOR_REPLY;
            case 2:
                return NEEDS_TO_REPLY;
            case 3:
                return APPROVED;
            case 4:
                return DECLINED;
        }

        return null;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int c) {
        this.code = c;
    }
}
