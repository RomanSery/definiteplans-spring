package com.coderdreams.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDateTime;


public class DefiniteDate implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String doingWhat;
    private String locationName;
    private String locationAddr;
    private LocalDateTime doingWhen;
    private int ownerUserId;
    private int dateeUserId;
    private LocalDateTime createdDate;
    private LocalDateTime ownerLastUpdate;
    private LocalDateTime dateeLastUpdate;
    private int ownerStatusId;
    private int dateeStatusId;
    private boolean emailReminderSent;
    private boolean postDateEmailSent;
    private int dateStatusId;
    private boolean ownerWantsMore;
    private boolean dateeWantsMore;
    private Boolean ownerWasSafe;
    private Boolean dateeWasSafe;
    private String ownerFeedback;
    private String dateeFeedback;
    private boolean ownerGaveFeedback;
    private boolean dateeGaveFeedback;
    private String greetingMsg;
    private boolean ownerNoShow;
    private boolean dateeNoShow;
    private String timezone;

    public DefiniteDate() {
    }

    public DefiniteDate(String timezone) {
        this.timezone = timezone;
    }


    public int getId() {
        return this.id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getDoingWhat() {
        return this.doingWhat;
    }


    public void setDoingWhat(String doingWhat) {
        this.doingWhat = doingWhat;
    }


    public String getLocationName() {
        return this.locationName;
    }


    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    public String getLocationAddr() {
        return this.locationAddr;
    }


    public void setLocationAddr(String locationAddr) {
        this.locationAddr = locationAddr;
    }


    public LocalDateTime getDoingWhen() {
        return this.doingWhen;
    }


    public void setDoingWhen(LocalDateTime doingWhen) {
        this.doingWhen = doingWhen;
    }


    public int getOwnerUserId() {
        return this.ownerUserId;
    }


    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }


    public int getDateeUserId() {
        return this.dateeUserId;
    }


    public void setDateeUserId(int dateeUserId) {
        this.dateeUserId = dateeUserId;
    }


    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }


    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


    public LocalDateTime getOwnerLastUpdate() {
        return this.ownerLastUpdate;
    }


    public void setOwnerLastUpdate(LocalDateTime ownerLastUpdate) {
        this.ownerLastUpdate = ownerLastUpdate;
    }


    public LocalDateTime getDateeLastUpdate() {
        return this.dateeLastUpdate;
    }


    public void setDateeLastUpdate(LocalDateTime dateeLastUpdate) {
        this.dateeLastUpdate = dateeLastUpdate;
    }


    public int getOwnerStatusId() {
        return this.ownerStatusId;
    }


    public void setOwnerStatusId(int ownerStatusId) {
        this.ownerStatusId = ownerStatusId;
    }


    public int getDateeStatusId() {
        return this.dateeStatusId;
    }


    public void setDateeStatusId(int dateeStatusId) {
        this.dateeStatusId = dateeStatusId;
    }


    public boolean isEmailReminderSent() {
        return this.emailReminderSent;
    }


    public void setEmailReminderSent(boolean emailReminderSent) {
        this.emailReminderSent = emailReminderSent;
    }


    public boolean isPostDateEmailSent() {
        return this.postDateEmailSent;
    }


    public void setPostDateEmailSent(boolean postDateEmailSent) {
        this.postDateEmailSent = postDateEmailSent;
    }


    public int getDateStatusId() {
        return this.dateStatusId;
    }


    public void setDateStatusId(int dateStatusId) {
        this.dateStatusId = dateStatusId;
    }


    public void setParticipantStatusId(boolean owner, int statusId) {
        if (owner) {
            this.ownerStatusId = statusId;
        } else {
            this.dateeStatusId = statusId;
        }
    }

    public void setParticipantLastUpdate(boolean owner, LocalDateTime lastUpdate) {
        if (owner) {
            this.ownerLastUpdate = lastUpdate;
        } else {
            this.dateeLastUpdate = lastUpdate;
        }
    }

    public LocalDateTime getParticipantLastUpdate(boolean owner) {
        if (owner) {
            return this.ownerLastUpdate;
        }
        return this.dateeLastUpdate;
    }


    public String toString() {
        return "DefiniteDate [id=" + this.id + ", doingWhat=" + this.doingWhat + ", locationName=" + this.locationName + ", locationAddr=" + this.locationAddr + ", doingWhen=" + this.doingWhen + ", ownerUserId=" + this.ownerUserId + ", dateeUserId=" + this.dateeUserId + ", createdDate=" + this.createdDate + ", ownerLastUpdate=" + this.ownerLastUpdate + ", dateeLastUpdate=" + this.dateeLastUpdate + ", ownerStatusId=" + this.ownerStatusId + ", dateeStatusId=" + this.dateeStatusId + ", emailReminderSent=" + this.emailReminderSent + ", postDateEmailSent=" + this.postDateEmailSent + ", dateStatusId=" + this.dateStatusId + "]";
    }


    public boolean isOwnerWantsMore() {
        return this.ownerWantsMore;
    }


    public void setOwnerWantsMore(boolean ownerWantsMore) {
        this.ownerWantsMore = ownerWantsMore;
    }


    public boolean isDateeWantsMore() {
        return this.dateeWantsMore;
    }


    public void setDateeWantsMore(boolean dateeWantsMore) {
        this.dateeWantsMore = dateeWantsMore;
    }


    public Boolean isOwnerWasSafe() {
        return this.ownerWasSafe;
    }


    public void setOwnerWasSafe(Boolean ownerWasSafe) {
        this.ownerWasSafe = ownerWasSafe;
    }


    public Boolean isDateeWasSafe() {
        return this.dateeWasSafe;
    }


    public void setDateeWasSafe(Boolean dateeWasSafe) {
        this.dateeWasSafe = dateeWasSafe;
    }


    public String getOwnerFeedback() {
        return this.ownerFeedback;
    }


    public void setOwnerFeedback(String ownerFeedback) {
        this.ownerFeedback = ownerFeedback;
    }


    public String getDateeFeedback() {
        return this.dateeFeedback;
    }


    public void setDateeFeedback(String dateeFeedback) {
        this.dateeFeedback = dateeFeedback;
    }


    public boolean isOwnerGaveFeedback() {
        return this.ownerGaveFeedback;
    }


    public void setOwnerGaveFeedback(boolean ownerGaveFeedback) {
        this.ownerGaveFeedback = ownerGaveFeedback;
    }


    public boolean isDateeGaveFeedback() {
        return this.dateeGaveFeedback;
    }


    public void setDateeGaveFeedback(boolean dateeGaveFeedback) {
        this.dateeGaveFeedback = dateeGaveFeedback;
    }


    public String getGreetingMsg() {
        return this.greetingMsg;
    }


    public void setGreetingMsg(String greetingMsg) {
        this.greetingMsg = greetingMsg;
    }


    public boolean isOwnerNoShow() {
        return this.ownerNoShow;
    }


    public void setOwnerNoShow(boolean ownerNoShow) {
        this.ownerNoShow = ownerNoShow;
    }


    public boolean isDateeNoShow() {
        return this.dateeNoShow;
    }


    public void setDateeNoShow(boolean dateeNoShow) {
        this.dateeNoShow = dateeNoShow;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
