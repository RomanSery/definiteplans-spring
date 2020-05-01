package com.definiteplans.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.definiteplans.controller.model.DateFeedback;
import com.definiteplans.controller.model.PastDateRow;
import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.DateParticipantStatus;
import com.definiteplans.dom.enumerations.DateStatus;
import com.definiteplans.dom.enumerations.SubmitType;
import com.definiteplans.util.DateUtil;

@Service
public class DateService {
    private final DefiniteDateRepository definiteDateRepository;
    private final ZipCodeRepository zipCodeRepository;
    private final UserRepository userRepository;

    public DateService(DefiniteDateRepository definiteDateRepository, ZipCodeRepository zipCodeRepository, UserRepository userRepository) {
        this.definiteDateRepository = definiteDateRepository;
        this.zipCodeRepository = zipCodeRepository;
        this.userRepository = userRepository;
    }

    public DefiniteDate createNew(User currUser, User profile) {
        Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
        DefiniteDate dd = new DefiniteDate(zip.isPresent() ? zip.get().getTimezone() : "America/New_York");
        dd.setOwnerUserId(currUser.getId());
        dd.setDateeUserId(profile.getId());
        return dd;
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

    public static LocalDateTime getSpecificTime(DefiniteDate dd) {
        LocalDateTime date = null;
        if(dd.getDoingWhenDate() != null) {
            date = LocalDateTime.of(dd.getDoingWhenDate(), LocalTime.MIDNIGHT);
        }
        if(dd.getDoingWhenTime() != null && date != null) {
            date = LocalDateTime.of(dd.getDoingWhenDate(), dd.getDoingWhenTime().toLocalTime());
        }
        return date;
    }


    public void setDateAttributes(ModelMap m, User currUser, User viewingUser, DefiniteDate dd) {

        m.addAttribute("past_dates", getPastDates(currUser, viewingUser));

        if(dd == null || dd.getId() == 0) {
            m.addAttribute("has_desc", false);
            m.addAttribute("can_edit", true);
            m.addAttribute("can_mod", false);
            m.addAttribute("show_feedback_form", false);
            return;
        }

        LocalDateTime date = getSpecificTime(dd);
        boolean isOwner = currUser.getId() == dd.getOwnerUserId();

        DateParticipantStatus myStatus = DateParticipantStatus.getById((dd.getOwnerUserId() == currUser.getId()) ? dd.getOwnerStatusId() : dd.getDateeStatusId());
        DateParticipantStatus otherPersonStatus = DateParticipantStatus.getById((dd.getOwnerUserId() == viewingUser.getId()) ? dd.getOwnerStatusId() : dd.getDateeStatusId());

        boolean enableFormFields = (myStatus == null || myStatus == DateParticipantStatus.NEEDS_TO_REPLY);
        boolean bothApproved = (dd.getOwnerStatusId() == DateParticipantStatus.APPROVED.getId() && dd.getDateeStatusId() == DateParticipantStatus.APPROVED.getId());
        boolean isTooLateToModify = (bothApproved && DateUtil.getHoursBetween(DateUtil.getCurrentServerTime(0, dd.getTimezone()), date, dd.getTimezone()) <= 6);

        boolean isTooLateToAccept = (date != null && DateUtil.isInThePast(date, dd.getTimezone()));
        boolean gaveFeedback = isOwner ? dd.isOwnerGaveFeedback() : dd.isDateeGaveFeedback();
        boolean theyGaveFeedback = isOwner ? dd.isDateeGaveFeedback() : dd.isOwnerGaveFeedback();

        boolean isChange = dd.getDateeLastUpdate() != null && dd.getOwnerLastUpdate() != null;
        boolean showFeedbackForm = dd.getId() > 0 && bothApproved && isTooLateToAccept && !gaveFeedback;

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

        m.addAttribute("date_desc", infoDesc);
        m.addAttribute("has_desc", !StringUtils.isBlank(infoDesc));
        m.addAttribute("can_edit", enableFormFields);
        m.addAttribute("can_mod", myStatus == DateParticipantStatus.NEEDS_TO_REPLY || dd.getDateStatusId() == DateStatus.APPROVED.getId());

        m.addAttribute("can_propose_change", !isTooLateToModify);
        m.addAttribute("can_accept", myStatus != DateParticipantStatus.APPROVED && !isTooLateToAccept);
        m.addAttribute("can_decline", !isTooLateToModify);
        m.addAttribute("show_feedback_form", showFeedbackForm);
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


    public boolean createDate(User currUser, DefiniteDate date) {
        if(currUser == null || date == null) {
            return false;
        }

        Optional<User> datee = userRepository.findById(date.getDateeUserId());
        if(datee.isEmpty()) {
            return false;
        }

        date.setCreatedDate(DateUtil.getCurrentServerTime());
        date.setDateStatusId(DateStatus.NEGOTIATION.getId());
        date.setOwnerUserId(currUser.getId());
        date.setOwnerLastUpdate(DateUtil.getCurrentServerTime());
        date.setOwnerStatusId(DateParticipantStatus.WAITING_FOR_REPLY.getId());
        date.setDateeUserId(datee.get().getId());
        date.setDateeStatusId(DateParticipantStatus.NEEDS_TO_REPLY.getId());
        date.setEmailReminderSent(false);

        Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
        date.setTimezone(zip.isPresent() ? zip.get().getTimezone() : "America/New_York");
        definiteDateRepository.save(date);

        //dateLetterManager.onDateUpdated(dd, type, DatePanel.this.isOwner(dd));

        return true;
    }


    public boolean updateDate(User currUser, SubmitType type, DefiniteDate date) {
        boolean isOwner = currUser.getId() == date.getOwnerUserId();

        date.setParticipantLastUpdate(isOwner, DateUtil.getCurrentServerTime());

        if (type == SubmitType.PROPOSE_CHANGE) {
            date.setEmailReminderSent(false);
            date.setDateStatusId(DateStatus.NEGOTIATION.getId());
            if (isOwner) {
                date.setOwnerStatusId(DateParticipantStatus.WAITING_FOR_REPLY.getId());
                date.setDateeStatusId(DateParticipantStatus.NEEDS_TO_REPLY.getId());
            } else {
                date.setDateeStatusId(DateParticipantStatus.WAITING_FOR_REPLY.getId());
                date.setOwnerStatusId(DateParticipantStatus.NEEDS_TO_REPLY.getId());
            }
        } else if (type == SubmitType.ACCEPT) {
            date.setParticipantStatusId(isOwner, DateParticipantStatus.APPROVED.getId());
            if (date.getOwnerStatusId() == DateParticipantStatus.APPROVED.getId() && date.getDateeStatusId() == DateParticipantStatus.APPROVED.getId()) {
                date.setDateStatusId(DateStatus.APPROVED.getId());
            } else {
                date.setDateStatusId(DateStatus.NEGOTIATION.getId());

                if (isOwner) {
                    date.setDateeStatusId(DateParticipantStatus.NEEDS_TO_REPLY.getId());
                } else {
                    date.setOwnerStatusId(DateParticipantStatus.NEEDS_TO_REPLY.getId());
                }
            }
        } else if (type == SubmitType.DECLINE) {
            date.setDateStatusId(DateStatus.DELETED.getId());
            date.setParticipantStatusId(isOwner, DateParticipantStatus.DECLINED.getId());
        }

        definiteDateRepository.save(date);

        //dateLetterManager.onDateUpdated(dd, type, DatePanel.this.isOwner(dd));

        return true;
    }



    public boolean submitDateFeedback(User currUser, DateFeedback feedback, DefiniteDate dd) {

        boolean isOwner = currUser.getId() == dd.getOwnerUserId();

        if (isOwner) {
            dd.setOwnerWantsMore(feedback.getParticipantWantsMore());
            dd.setOwnerWasSafe(feedback.getParticipantWasSafe());
            dd.setOwnerFeedback(feedback.getParticipantFeedback());
            dd.setDateeNoShow(feedback.getParticipantNoShow());
            dd.setOwnerGaveFeedback(true);
        } else {
            dd.setDateeWantsMore(feedback.getParticipantWantsMore());
            dd.setDateeWasSafe(feedback.getParticipantWasSafe());
            dd.setDateeFeedback(feedback.getParticipantFeedback());
            dd.setOwnerNoShow(feedback.getParticipantNoShow());
            dd.setDateeGaveFeedback(true);
        }

        if (dd.isOwnerGaveFeedback() && dd.isDateeGaveFeedback()) {
            dd.setDateStatusId(DateStatus.OCCURRED.getId());
        }
        definiteDateRepository.save(dd);

        if (isOwner && dd.isDateeNoShow()) {
            Optional<User> datee = userRepository.findById(dd.getDateeUserId());
            if(datee.isPresent()) {
                datee.get().setNumNoShows(datee.get().getNumNoShows() + 1);
                userRepository.save(datee.get());
            }
        }

        if (!isOwner && dd.isOwnerNoShow()) {
            Optional<User> owner = userRepository.findById(dd.getOwnerUserId());
            if(owner.isPresent()) {
                owner.get().setNumNoShows(owner.get().getNumNoShows() + 1);
                userRepository.save(owner.get());
            }
        }

        //dateLetterManager.onSubmittedPostDateFeedback(dd, isOwner(dd));
        return true;
    }
}
