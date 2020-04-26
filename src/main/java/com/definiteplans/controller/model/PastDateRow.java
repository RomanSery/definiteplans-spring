package com.definiteplans.controller.model;


import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PastDateRow implements Serializable {
    private static final long serialVersionUID=1L;

    private final String doingWhat;
    private final LocalDateTime doingWhen;
    private final String locationName;
    private final String greetingMsg;
    private final boolean wantsAnotherDate;
    private final String theirName;

    public PastDateRow(String doingWhat, LocalDateTime doingWhen, String locationName, String greetingMsg, boolean wantsAnotherDate, String theirName) {
        this.doingWhat = doingWhat;
        this.doingWhen = doingWhen;
        this.locationName = locationName;
        this.greetingMsg = greetingMsg;
        this.wantsAnotherDate = wantsAnotherDate;
        this.theirName = theirName;
    }
}
