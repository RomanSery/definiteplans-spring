package com.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.definiteplans.controller.model.DateProposal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "definite_date")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DefiniteDate implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "date_id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "doing_what")
    private String doingWhat;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "doing_when")
    private LocalDateTime doingWhen;

    @Column(name = "owner_id")
    private int ownerUserId;

    @Column(name = "datee_id")
    private int dateeUserId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "owner_last_update")
    private LocalDateTime ownerLastUpdate;

    @Column(name = "datee_last_update")
    private LocalDateTime dateeLastUpdate;

    @Column(name = "owner_status_id")
    private int ownerStatusId;

    @Column(name = "datee_status_id")
    private int dateeStatusId;

    @Column(name = "date_status_id")
    private int dateStatusId;

    @Column(name = "owner_wants_more")
    private boolean ownerWantsMore;

    @Column(name = "datee_wants_more")
    private boolean dateeWantsMore;

    @Column(name = "owner_was_safe")
    private Boolean ownerWasSafe;

    @Column(name = "datee_was_safe")
    private Boolean dateeWasSafe;

    @Column(name = "owner_feedback")
    private String ownerFeedback;

    @Column(name = "datee_feedback")
    private String dateeFeedback;

    @Column(name = "owner_gave_feedback")
    private boolean ownerGaveFeedback;

    @Column(name = "datee_gave_feedback")
    private boolean dateeGaveFeedback;

    @Column(name = "greeting_msg")
    private String greetingMsg;

    @Column(name = "owner_no_show")
    private boolean ownerNoShow;

    @Column(name = "datee_no_show")
    private boolean dateeNoShow;

    public DefiniteDate() {
        this.doingWhen = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(18, 0));
    }

    public DefiniteDate(DateProposal proposal) {
        this.doingWhat = proposal.getDoingWhat();
        this.locationName = proposal.getLocationName();
        this.doingWhen = LocalDateTime.of(proposal.getDoingWhenDate(), proposal.getDoingWhenTime());
        this.greetingMsg = proposal.getGreetingMsg();
        this.ownerUserId = proposal.getOwnerUserId();
        this.dateeUserId = proposal.getDateeUserId();
    }

    public void updateFromProposal(DateProposal proposal) {
        this.doingWhat = proposal.getDoingWhat();
        this.locationName = proposal.getLocationName();
        this.doingWhen = LocalDateTime.of(proposal.getDoingWhenDate(), proposal.getDoingWhenTime());
        this.greetingMsg = proposal.getGreetingMsg();
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

}
