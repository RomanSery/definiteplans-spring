package com.definiteplans.controller.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRow implements Serializable {
    private static final long serialVersionUID=1L;

    private final int userId;
    private final String displayName;
    private final String img;
    private final String msg;
    private final String date;


    public ChatRow(int userId, String displayName, String img, String msg, String date) {
        this.userId = userId;
        this.displayName = displayName;
        this.img = img;
        this.msg = msg;
        this.date = date;
    }
}
