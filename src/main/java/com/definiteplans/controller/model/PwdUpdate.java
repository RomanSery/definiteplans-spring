package com.definiteplans.controller.model;


import java.io.Serializable;

public class PwdUpdate implements Serializable {
    private static final long serialVersionUID=1L;

    private String currPwd;
    private String password1;
    private String password2;

    public PwdUpdate() {

    }

    public String getCurrPwd() {
        return currPwd;
    }

    public void setCurrPwd(String currPwd) {
        this.currPwd = currPwd;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
