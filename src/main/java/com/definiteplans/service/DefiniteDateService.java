package com.definiteplans.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.definiteplans.controller.model.PastDateRow;
import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.DateParticipantStatus;
import com.definiteplans.util.DateUtil;

@Service
public class DefiniteDateService {
    private final DefiniteDateRepository definiteDateRepository;
    private final ZipCodeRepository zipCodeRepository;

    public DefiniteDateService(DefiniteDateRepository definiteDateRepository, ZipCodeRepository zipCodeRepository) {
        this.definiteDateRepository = definiteDateRepository;
        this.zipCodeRepository = zipCodeRepository;
    }

    public DefiniteDate createNew(User currUser) {
        Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
        return new DefiniteDate(zip.isPresent() ? zip.get().getTimezone() : "America/New_York");
    }


    public Boolean wantsMore(User currUser, User viewing, DefiniteDate activeDate) {
        if (activeDate != null) {
            return null;
        }
        List<DefiniteDate> pastDates = definiteDateRepository.getPastDatesByOwnerAndDatee(currUser.getId(), viewing.getId());
        if (pastDates.size() > 0) {
            DefiniteDate dd = pastDates.get(0);
            if (viewing.getId() == dd.getOwnerUserId()) {
                return Boolean.valueOf(dd.isOwnerWantsMore());
            }
            return Boolean.valueOf(dd.isDateeWantsMore());
        }
        return null;
    }

    private LocalDateTime getSpecificTime(DefiniteDate dd) {
        LocalDateTime date = null;
        if(dd.getDoingWhenDate() != null) {
            date = LocalDateTime.of(dd.getDoingWhenDate(), LocalTime.MIDNIGHT);
        }
        if(dd.getDoingWhenTime() != null && date != null) {
            date = LocalDateTime.of(dd.getDoingWhenDate(), dd.getDoingWhenTime());
        }
        return date;
    }


    public String getDateDescription(User currUser, User viewingUser, DefiniteDate dd) {

        if(dd == null || dd.getId() == 0) {
            return "";
        }

        LocalDateTime date = getSpecificTime(dd);
        boolean isOwner = currUser.getId() == dd.getOwnerUserId();

        DateParticipantStatus myStatus = DateParticipantStatus.getById((dd.getOwnerUserId() == currUser.getId()) ? dd.getOwnerStatusId() : dd.getDateeStatusId());
        DateParticipantStatus otherPersonStatus = DateParticipantStatus.getById((dd.getOwnerUserId() == viewingUser.getId()) ? dd.getOwnerStatusId() : dd.getDateeStatusId());

        boolean bothApproved = (dd.getOwnerStatusId() == DateParticipantStatus.APPROVED.getId() && dd.getDateeStatusId() == DateParticipantStatus.APPROVED.getId());
        boolean isTooLateToModify = (bothApproved && DateUtil.getHoursBetween(DateUtil.getCurrentServerTime(0, dd.getTimezone()), date, dd.getTimezone()) <= 6);

        boolean isTooLateToAccept = (date != null && DateUtil.isInThePast(date, dd.getTimezone()));
        boolean gaveFeedback = isOwner ? dd.isOwnerGaveFeedback() : dd.isDateeGaveFeedback();
        boolean theyGaveFeedback = isOwner ? dd.isDateeGaveFeedback() : dd.isOwnerGaveFeedback();

        boolean isChange = dd.getDateeLastUpdate() != null && dd.getOwnerLastUpdate() != null;


        String infoDesc = "";
        if (bothApproved) {
            if (isTooLateToAccept) {
                if (gaveFeedback) {
                    infoDesc = theyGaveFeedback ? "You both gave your feeback" : ("Thank you for providing your feedback.  Please wait for " + viewingUser.getDisplayName() + "'s feedback.");
                } else {
                    infoDesc = "Please give your feedback about the date below:";
                }
            } else if (isTooLateToModify) {
                infoDesc = "You have both accepted, have fun on the date and then come back to give your feedback!";
            } else {
                infoDesc = "You have both accepted!  You can still make a change if you need to, up until 6 hours from the start time.";
            }
        } else {
            String change = isChange ? " change " : " ";
            if (myStatus == DateParticipantStatus.WAITING_FOR_REPLY) {
                infoDesc = "You proposed this" + change + "on " + DateUtil.printDateTime(dd.getParticipantLastUpdate(isOwner), dd.getTimezone()) + ".  Waiting for " + viewingUser.getDisplayName() + " to reply.";
            } else if (myStatus == DateParticipantStatus.NEEDS_TO_REPLY) {
                if (otherPersonStatus == DateParticipantStatus.APPROVED) {
                    infoDesc = viewingUser.getDisplayName() + " has accepted this on " + DateUtil.printDateTime((viewingUser.getId() == dd.getOwnerUserId()) ? dd.getOwnerLastUpdate() : dd.getDateeLastUpdate(), dd.getTimezone());
                } else {
                    infoDesc = viewingUser.getDisplayName() + " has proposed this" + change + "on " + DateUtil.printDateTime((viewingUser.getId() == dd.getOwnerUserId()) ? dd.getOwnerLastUpdate() : dd.getDateeLastUpdate(), dd.getTimezone());
                }
            } else if (myStatus == DateParticipantStatus.APPROVED) {
                infoDesc = "You have accepted this on " + DateUtil.printDateTime((viewingUser.getId() == dd.getOwnerUserId()) ? dd.getOwnerLastUpdate() : dd.getDateeLastUpdate(), dd.getTimezone()) + ".  Waiting for " + viewingUser.getDisplayName() + " to reply.";
            }
        }
        return infoDesc;
    }

    public List<PastDateRow> getPastDates(User currUser, User profile) {
        List<DefiniteDate> pastDates = definiteDateRepository.getPastDatesByOwnerAndDatee(currUser.getId(), profile.getId());

        List<PastDateRow> rows = new ArrayList<>(pastDates.size());
        for(DefiniteDate dd : pastDates) {

            String doingWhat = dd.getDoingWhat();
            LocalDateTime doingWhen = getSpecificTime(dd);
            String locationName = dd.getLocationName();
            String greetingMsg = '"' + dd.getGreetingMsg() + '"';
            boolean wantsAnotherDate = dd.getOwnerUserId() == profile.getId() ? dd.isOwnerWantsMore() : dd.isDateeWantsMore();
            String theirName = dd.getOwnerUserId() == profile.getId() ? profile.getDisplayName() : currUser.getDisplayName();
            rows.add(new PastDateRow(doingWhat, doingWhen, locationName, greetingMsg, wantsAnotherDate, theirName));
        }
        return rows;
    }

}
