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
    private final String desc2;
    private final String desc3;
    private final String date;

    public ActionItem(String url, String desc) {
        this(url, desc, null, null, null);
    }

    public ActionItem(String url, String desc, String desc2, String desc3, String date) {
        this.url = url;
        this.desc = desc;
        this.desc2 = desc2;
        this.desc3 = desc3;
        this.date = date;
    }
}
