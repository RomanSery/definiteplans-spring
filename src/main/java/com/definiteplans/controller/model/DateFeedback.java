package com.definiteplans.controller.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateFeedback implements Serializable {
    private static final long serialVersionUID=1L;

    private int dateId;
    private Boolean participantWantsMore;
    private Boolean participantWasSafe;
    private Boolean participantNoShow;
    private String participantFeedback;

    public DateFeedback(int dateId) {
        this.dateId = dateId;
    }

}
