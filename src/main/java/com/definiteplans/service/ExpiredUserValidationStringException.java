package com.definiteplans.service;


public class ExpiredUserValidationStringException
        extends Exception {
    private static final long serialVersionUID = 1L;
    private String email;

    public ExpiredUserValidationStringException() {
    }

    public ExpiredUserValidationStringException(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
