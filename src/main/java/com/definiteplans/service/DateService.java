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
import com.definiteplans.controller.model.DateProposal;
import com.definiteplans.controller.model.PastDateRow;
import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.dom.enumerations.DateParticipantStatus;
import com.definiteplans.dom.enumerations.DateStatus;
import com.definiteplans.dom.enumerations.SubmitType;
import com.definiteplans.util.DateUtil;

import static com.definiteplans.dom.enumerations.DateParticipantStatus.NEEDS_TO_REPLY;
import static com.definiteplans.dom.enumerations.DateParticipantStatus.WAITING_FOR_REPLY;

@Service
public class DateService {
    private final DefiniteDateRepository definiteDateRepository;
    private final UserRepository userRepository;

    public DateService(DefiniteDateRepository definiteDateRepository, UserRepository userRepository) {
        this.definiteDateRepository = definiteDateRepository;
        this.userRepository = userRepository;
    }

    public DefiniteDate createNew(User currUser, User profile) {
        DefiniteDate dd = new DefiniteDate();
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


    public static LocalDateTime getSpecificTime(DateProposal dd) {
        LocalDateTime date = null;
        if(dd.getDoingWhenDate() != null) {
            date = LocalDateTime.of(dd.getDoingWhenDate(), LocalTime.MIDNIGHT);
        }
        if(dd.getDoingWhenTime() != null && date != null) {
            date = LocalDateTime.of(dd.getDoingWhenDate(), dd.getDoingWhenTime());
        }
        return date;
    }

    private String getDateInfo(DefiniteDate date) {
        List<String> arr = new ArrayList<>();
        arr.add(date.getDoingWhat());
        arr.add(date.getLocationName());
        arr.add(DateUtil.printDateTime(date.getDoingWhen()));
        return StringUtils.join(arr, "<br>");
    }

    public void setDateAttributes(ModelMap m, User currUser, User viewingUser, DefiniteDate dd) {

        m.addAttribute("past_dates", getPastDates(currUser, viewingUser));
        m.addAttribute("date_info", getDateInfo(dd));

        if(dd == null || dd.getId() == 0) {
            m.addAttribute("has_desc", false);
            m.addAttribute("can_edit", true);
            m.addAttribute("can_mod", false);
            m.addAttribute("show_feedback_form", false);
            return;
        }

        LocalDateTime doingWhen = dd.getDoingWhen();
        boolean isOwner = currUser.getId() == dd.getOwnerUserId();

        DateParticipantStatus myStatus = DateParticipantStatus.getById((dd.getOwnerUserId() == currUser.getId()) ? dd.getOwnerStatusId() : dd.getDateeStatusId());
        DateParticipantStatus otherPersonStatus = DateParticipantStatus.getById((dd.getOwnerUserId() == viewingUser.getId()) ? dd.getOwnerStatusId() : dd.getDateeStatusId());

        boolean bothApproved = (dd.getOwnerStatusId() == DateParticipantStatus.ACCEPTED.getId() && dd.getDateeStatusId() == DateParticipantStatus.ACCEPTED.getId());
        boolean isTooLateToModify = (bothApproved && DateUtil.getHoursBetween(DateUtil.getCurrentServerTime(), doingWhen) <= 6);

        boolean isTooLateToAccept = (doingWhen != null && DateUtil.isInThePast(doingWhen));
        boolean gaveFeedback = isOwner ? dd.isOwnerGaveFeedback() : dd.isDateeGaveFeedback();
        boolean theyGaveFeedback = isOwner ? dd.isDateeGaveFeedback() : dd.isOwnerGaveFeedback();

        boolean isChange = dd.getDateeLastUpdate() != null && dd.getOwnerLastUpdate() != null;
        boolean showFeedbackForm = dd.getId() > 0 && bothApproved && isTooLateToAccept && !gaveFeedback;

        boolean canEdit = !isTooLateToAccept && ((myStatus == null || myStatus == NEEDS_TO_REPLY) || (bothApproved && !isTooLateToModify));

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
            if (myStatus == WAITING_FOR_REPLY) {
                infoDesc = "You proposed this" + change + "on " + DateUtil.printDateTime(dd.getParticipantLastUpdate(isOwner)) + ".  Waiting for " + viewingUser.getDisplayName() + " to reply.";
            } else if (myStatus == NEEDS_TO_REPLY) {
                if (otherPersonStatus == DateParticipantStatus.ACCEPTED) {
                    infoDesc = viewingUser.getDisplayName() + " has accepted this on " + DateUtil.printDateTime((viewingUser.getId() == dd.getOwnerUserId()) ? dd.getOwnerLastUpdate() : dd.getDateeLastUpdate());
                } else {
                    infoDesc = viewingUser.getDisplayName() + " has proposed this" + change + "on " + DateUtil.printDateTime((viewingUser.getId() == dd.getOwnerUserId()) ? dd.getOwnerLastUpdate() : dd.getDateeLastUpdate());
                }
            } else if (myStatus == DateParticipantStatus.ACCEPTED) {
                infoDesc = "You have accepted this on " + DateUtil.printDateTime((viewingUser.getId() == dd.getOwnerUserId()) ? dd.getOwnerLastUpdate() : dd.getDateeLastUpdate()) + ".  Waiting for " + viewingUser.getDisplayName() + " to reply.";
            }
        }

        m.addAttribute("date_desc", infoDesc);
        m.addAttribute("has_desc", !StringUtils.isBlank(infoDesc));
        m.addAttribute("can_edit", canEdit);
        m.addAttribute("can_mod", (myStatus == NEEDS_TO_REPLY || dd.getDateStatusId() == DateStatus.APPROVED.getId()) && !isTooLateToAccept);

        m.addAttribute("can_propose_change", !isTooLateToModify);
        m.addAttribute("can_accept", myStatus != DateParticipantStatus.ACCEPTED && !isTooLateToAccept);
        m.addAttribute("can_decline", !isTooLateToModify);
        m.addAttribute("show_feedback_form", showFeedbackForm);
    }

    public List<PastDateRow> getPastDates(User currUser, User profile) {
        List<DefiniteDate> pastDates = definiteDateRepository.getPastDatesByOwnerAndDatee(currUser.getId(), profile.getId());

        List<PastDateRow> rows = new ArrayList<>(pastDates.size());
        for(DefiniteDate dd : pastDates) {

            String doingWhat = dd.getDoingWhat();
            LocalDateTime doingWhen = dd.getDoingWhen();
            String locationName = dd.getLocationName();
            String greetingMsg = '"' + dd.getGreetingMsg() + '"';
            boolean wantsAnotherDate = dd.getOwnerUserId() == profile.getId() ? dd.isOwnerWantsMore() : dd.isDateeWantsMore();
            String theirName = dd.getOwnerUserId() == profile.getId() ? profile.getDisplayName() : currUser.getDisplayName();
            rows.add(new PastDateRow(doingWhat, doingWhen, locationName, greetingMsg, wantsAnotherDate, theirName));
        }
        return rows;
    }


    public boolean createDate(User currUser, DateProposal proposal) {
        if(currUser == null || proposal == null) {
            return false;
        }

        Optional<User> datee = userRepository.findById(proposal.getDateeUserId());
        if(datee.isEmpty()) {
            return false;
        }

        DefiniteDate date = new DefiniteDate(proposal);

        date.setCreatedDate(DateUtil.getCurrentServerTime());
        date.setDateStatusId(DateStatus.NEGOTIATION.getId());
        date.setOwnerUserId(currUser.getId());
        date.setOwnerLastUpdate(DateUtil.getCurrentServerTime());
        date.setOwnerStatusId(WAITING_FOR_REPLY.getId());
        date.setDateeUserId(datee.get().getId());
        date.setDateeStatusId(NEEDS_TO_REPLY.getId());
        date.setEmailReminderSent(false);

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
                date.setOwnerStatusId(WAITING_FOR_REPLY.getId());
                date.setDateeStatusId(NEEDS_TO_REPLY.getId());
            } else {
                date.setDateeStatusId(WAITING_FOR_REPLY.getId());
                date.setOwnerStatusId(NEEDS_TO_REPLY.getId());
            }
        } else if (type == SubmitType.ACCEPT) {
            date.setParticipantStatusId(isOwner, DateParticipantStatus.ACCEPTED.getId());

            if(isOwner && date.getDateeStatusId() == WAITING_FOR_REPLY.getId()) {
                date.setDateeStatusId(DateParticipantStatus.ACCEPTED.getId());
            }
            if(!isOwner && date.getOwnerStatusId() == WAITING_FOR_REPLY.getId()) {
                date.setOwnerStatusId(DateParticipantStatus.ACCEPTED.getId());
            }

            if (date.getOwnerStatusId() == DateParticipantStatus.ACCEPTED.getId() && date.getDateeStatusId() == DateParticipantStatus.ACCEPTED.getId()) {
                date.setDateStatusId(DateStatus.APPROVED.getId());
            } else {
                date.setDateStatusId(DateStatus.NEGOTIATION.getId());

                if (isOwner) {
                    date.setDateeStatusId(NEEDS_TO_REPLY.getId());
                } else {
                    date.setOwnerStatusId(NEEDS_TO_REPLY.getId());
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
