package com.definiteplans.controller.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionItem implements Serializable {
    private static final long serialVersionUID=1L;

    private final String url;
    private final String desc;

    public ActionItem(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }
}
