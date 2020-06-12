package com.definiteplans.dom.enumerations;

import java.util.List;

public enum LoginErrorType implements GenericEnum {
    NOT_FOUND(1, "The login credentials you entered do not match our records. <br> Please verify your entry and try again or click 'Forgot Password?' below."),
    DISABLED(2, "Your account has been disabled."),
    PENDING(3, "You have not confirmed your email address yet, please do that first."),
    FB_NO_PWD(4, "You created this account thru Facebook.  Try logging with Facebook."),
    GOOGLE_NO_PWD(5, "You created this account thru Google.  Try logging with Google.")
    ;

    public static final List<LoginErrorType> VALUES = List.of(LoginErrorType.values());

    private final int id;
    private String description;

    LoginErrorType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static LoginErrorType getById(int id) {
        return VALUES.stream().filter(dt -> dt.id == id).findFirst().orElse(null);
    }

    @Override public void setDescription(String description) { this.description = description; }
    @Override public String getDescription() { return description; }
    @Override public int getId() { return id; }
}
