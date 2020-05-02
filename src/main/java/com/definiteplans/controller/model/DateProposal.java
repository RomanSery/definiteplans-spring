package com.definiteplans.controller.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.definiteplans.dom.DefiniteDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateProposal implements Serializable {
    private static final long serialVersionUID=1L;

    private int id;
    private String doingWhat;
    private String locationName;
    private String greetingMsg;
    private int ownerUserId;
    private int dateeUserId;

    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private LocalDate doingWhenDate;

    @DateTimeFormat(pattern = "h:mm a")
    private LocalTime doingWhenTime;

    public DateProposal() {

    }

    public DateProposal(DefiniteDate dd) {
        this.id = dd.getId();
        this.doingWhat = dd.getDoingWhat();
        this.locationName = dd.getLocationName();
        if(dd.getDoingWhen() != null) {
            this.doingWhenDate = dd.getDoingWhen().toLocalDate();
            this.doingWhenTime = dd.getDoingWhen().toLocalTime();
        }

        this.greetingMsg = dd.getGreetingMsg();
        this.ownerUserId = dd.getOwnerUserId();
        this.dateeUserId = dd.getDateeUserId();
    }

}
