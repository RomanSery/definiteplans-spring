package com.definiteplans.controller.model;

public class AjaxResponse {
    private final String status;
    private final String msg;

    public AjaxResponse(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
