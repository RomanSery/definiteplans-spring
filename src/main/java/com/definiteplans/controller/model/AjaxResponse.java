package com.definiteplans.controller.model;

import java.util.List;

public final class AjaxResponse {
    private final String status;
    private final String msg;

    private final static String errTemplate = "<ul class=\"list-unstyled spaced\">{errors}</ul>";

    private AjaxResponse(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static AjaxResponse success(String msg) {
        return new AjaxResponse("OK", msg);
    }
    public static AjaxResponse error(List<String> errors) {
        StringBuilder sb = new StringBuilder();
        for(String err : errors) {
            sb.append("<li>");
            sb.append(err);
            sb.append("</li>");
        }
        return new AjaxResponse("ERR", errTemplate.replace("{errors}", sb.toString()));
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
