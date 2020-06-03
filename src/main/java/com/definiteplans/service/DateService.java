package com.definiteplans.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.definiteplans.controller.model.ActionItem;
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
import com.definiteplans.email.EmailService;
import com.definiteplans.util.DateUtil;

import static com.definiteplans.dom.enumerations.DateParticipantStatus.NEEDS_TO_REPLY;
import static com.definiteplans.dom.enumerations.DateParticipantStatus.WAITING_FOR_REPLY;

@Service
public class DateService {
    private final DefiniteDateRepository definiteDateRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserService userService;

    public DateService(DefiniteDateRepository definiteDateRepository, UserRepository userRepository, EmailService emailService, UserService userService) {
        this.definiteDateRepository = definiteDateRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userService = userService;
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
        boolean isTooLateToModify = (bothApproved && DateUtil.getHoursBetween(DateUtil.now(), doingWhen) <= 6);

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
            String doingWhen = DateUtil.printDateTime(dd.getDoingWhen());
            String locationName = dd.getLocationName();
            String greetingMsg = '"' + dd.getGreetingMsg() + '"';
            boolean wantsAnotherDate = dd.getOwnerUserId() == profile.getId() ? dd.isOwnerWantsMore() : dd.isDateeWantsMore();
            String theirName = profile.getDisplayName();
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

        if(!userService.canViewProfile(currUser, datee.get())) {
            return false;
        }

        DefiniteDate date = new DefiniteDate(proposal);

        date.setCreatedDate(DateUtil.now());
        date.setDateStatusId(DateStatus.NEGOTIATION.getId());
        date.setOwnerUserId(currUser.getId());
        date.setOwnerLastUpdate(DateUtil.now());
        date.setOwnerStatusId(WAITING_FOR_REPLY.getId());
        date.setDateeUserId(datee.get().getId());
        date.setDateeStatusId(NEEDS_TO_REPLY.getId());
        date.setEmailReminderSent(false);

        definiteDateRepository.save(date);

        onDateUpdated(date, currUser, SubmitType.PROPOSE_NEW_PLAN);

        return true;
    }


    public boolean updateDate(User currUser, SubmitType type, DefiniteDate date) {
        boolean isOwner = currUser.getId() == date.getOwnerUserId();

        User otherUser = getOtherUser(date, isOwner);
        if(!userService.canViewProfile(currUser, otherUser)) {
            return false;
        }

        date.setParticipantLastUpdate(isOwner, DateUtil.now());

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

        onDateUpdated(date, currUser, type);

        return true;
    }


    public void onBlockUser(User currUser, int userToBlock) {
        DefiniteDate dd = definiteDateRepository.getActiveDate(currUser.getId(), userToBlock);
        if (dd != null) {
            boolean isOwner = (currUser.getId() == dd.getOwnerUserId());
            dd.setParticipantLastUpdate(isOwner, DateUtil.now());
            dd.setDateStatusId(DateStatus.DELETED.getId());
            dd.setParticipantStatusId(isOwner, DateParticipantStatus.DECLINED.getId());
            definiteDateRepository.save(dd);
            onDateUpdated(dd, currUser, SubmitType.DECLINE);
        }
    }


    public boolean submitDateFeedback(User currUser, DateFeedback feedback, DefiniteDate dd) {

        boolean isOwner = currUser.getId() == dd.getOwnerUserId();

        User otherUser = getOtherUser(dd, isOwner);
        if(!userService.canViewProfile(currUser, otherUser)) {
            return false;
        }

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

        onSubmittedPostDateFeedback(dd, currUser);
        return true;
    }

    public List<ActionItem> getRequiresMyAction(User currUser, List<DefiniteDate> upComingDates) {

        List<ActionItem> toReturn = new ArrayList<>();

        for(DefiniteDate dd : upComingDates) {

            int profileId = currUser.getId() == dd.getOwnerUserId() ? dd.getDateeUserId() : dd.getOwnerUserId();
            Optional<User> found = userRepository.findById(profileId);
            if(found.isEmpty()) {
                continue;
            }
            String displayName = found.get().getDisplayName();

            LocalDateTime doingWhen = dd.getDoingWhen();
            boolean isOwner = currUser.getId() == dd.getOwnerUserId();

            DateParticipantStatus myStatus = DateParticipantStatus.getById((dd.getOwnerUserId() == currUser.getId()) ? dd.getOwnerStatusId() : dd.getDateeStatusId());
            boolean isTooLateToAccept = (doingWhen != null && DateUtil.isInThePast(doingWhen));
            boolean gaveFeedback = isOwner ? dd.isOwnerGaveFeedback() : dd.isDateeGaveFeedback();

            String actionDesc = null;
            if (dd.getDateStatusId() == DateStatus.NEGOTIATION.getId() && myStatus == DateParticipantStatus.NEEDS_TO_REPLY) {
                actionDesc = "You should reply to " + displayName +"'s date proposal";
            }
            if (dd.getDateStatusId() == DateStatus.APPROVED.getId() && isTooLateToAccept && !gaveFeedback) {
                actionDesc = "Please give feedback for your date with " + displayName;
            }

            if (StringUtils.isBlank(actionDesc)) {
                continue;
            }


            String url = "/profiles/" + profileId;
            toReturn.add(new ActionItem(url, actionDesc));
        }

        return toReturn;
    }


    public List<ActionItem> getUpComingDatesDetail(User currUser, List<DefiniteDate> upComingDates) {

        List<ActionItem> toReturn = new ArrayList<>();

        for(DefiniteDate dd : upComingDates) {

            int profileId = currUser.getId() == dd.getOwnerUserId() ? dd.getDateeUserId() : dd.getOwnerUserId();
            Optional<User> found = userRepository.findById(profileId);
            if(found.isEmpty()) {
                continue;
            }
            String displayName = found.get().getDisplayName();

            LocalDateTime doingWhen = dd.getDoingWhen();
            String actionDesc = "Date with " + displayName;
            String desc2 = dd.getDoingWhat();
            String desc3 = dd.getLocationName();

            String url = "/profiles/" + profileId;
            toReturn.add(new ActionItem(url, actionDesc, desc2, desc3, DateUtil.printDateTime(doingWhen)));
        }

        return toReturn;
    }




    private User getToUser(DefiniteDate date, boolean isOwner) {
        Optional<User> found;
        if (isOwner) {
            found = userRepository.findById(date.getDateeUserId());
        } else {
            found = userRepository.findById(date.getOwnerUserId());
        }
        return found.isPresent() ? found.get() : null;
    }
    private User getOtherUser(DefiniteDate date, boolean isOwner) {
        Optional<User> found;
        if (isOwner) {
            found = userRepository.findById(date.getOwnerUserId());
        } else {
            found = userRepository.findById(date.getDateeUserId());
        }
        return found.isPresent() ? found.get() : null;
    }


    private void onSubmittedPostDateFeedback(DefiniteDate date, User currUser) {

        boolean isOwner = (currUser.getId() == date.getOwnerUserId());
        User toUser = getToUser(date, isOwner);
        if (toUser == null || !toUser.isSendNotifications()) {
            return;
        }

        User otherUser = getOtherUser(date, isOwner);
        if(otherUser == null) {
            return;
        }

        Map<String, String> context = new HashMap<>();
        context.put("toName", toUser.getDisplayName());
        context.put("otherPersonName", otherUser.getDisplayName());

        String subject = otherUser.getDisplayName() + "'s post-date feedback";
        boolean wantsMore = isOwner ? date.isOwnerWantsMore() : date.isDateeWantsMore();
        String template = wantsMore ? "date_onSubmittedPostDateFeedback_WantsMore.fmt" : "date_onSubmittedPostDateFeedback_NoMore.fmt";

        emailService.sendEmail(template, context, subject, toUser);
    }

    private void onDateUpdated(DefiniteDate date, User currUser, SubmitType type) {
        boolean isOwner = (currUser.getId() == date.getOwnerUserId());

        User toUser = getToUser(date, isOwner);
        if (toUser == null || !toUser.isSendNotifications()) {
            return;
        }

        User otherUser = getOtherUser(date, isOwner);
        if(otherUser == null) {
            return;
        }

        Map<String, String> context = new HashMap<>();
        context.put("toName", toUser.getDisplayName());
        context.put("otherPersonName", otherUser.getDisplayName());
        context.put("greeting", date.getGreetingMsg());
        context.put("what", date.getDoingWhat());
        context.put("whereName", date.getLocationName());
        context.put("dateWhen", DateUtil.printDateTime(date.getDoingWhen()));

        UriComponents uriComponents = UriComponentsBuilder.fromPath(EmailService.getBaseUrl() + "/profiles/" + otherUser.getId()).build();
        context.put("viewProfileUrl", uriComponents.toUriString());

        String template = null;
        String subject = null;
        if (type == SubmitType.PROPOSE_NEW_PLAN) {
            context.put("otherPersonNameMsg", otherUser.getDisplayName() + " has proposed the following date:");
            subject = otherUser.getDisplayName() + " has proposed a date";
            template = "date_onDateUpdate.fmt";
        } else if (type == SubmitType.PROPOSE_CHANGE) {
            context.put("otherPersonNameMsg", otherUser.getDisplayName() + " has proposed the following change to your date:");
            subject = otherUser.getDisplayName() + " has proposed a change to your date";
            template = "date_onDateUpdate.fmt";
        } else if (type == SubmitType.ACCEPT) {
            context.put("otherPersonNameMsg", otherUser.getDisplayName() + " has accepted the following date:");
            subject = otherUser.getDisplayName() + " has accepted your date";
            template = "date_onDateUpdate.fmt";
        } else if (type == SubmitType.DECLINE) {
            subject = otherUser.getDisplayName() + " has declined your date";
            template = "date_onDeclineDate.fmt";
        }

        emailService.sendEmail(template, context, subject, toUser);
    }
}
