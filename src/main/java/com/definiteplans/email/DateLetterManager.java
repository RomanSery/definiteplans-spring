package com.definiteplans.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.enumerations.SubmitType;
import com.definiteplans.service.UnsubscriberService;
import com.definiteplans.service.UserService;


@Service
public class DateLetterManager {
    private static final Logger logger = LoggerFactory.getLogger(DateLetterManager.class);

    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;
    @Autowired
    UnsubscriberService unsubscriberService;

    private final UserRepository userRepository;

    private String serverAbsoluteUrlPrefix = null;

    public DateLetterManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void onSubmittedPostDateFeedback(DefiniteDate dd, boolean isOwner) {
//        try {
//            Optional<User> toUser = userRepository.findById(isOwner ? dd.getDateeUserId() : dd.getOwnerUserId());
//
//            if (!toUser.isSendNotifications())
//                return;
//            if (this.unsubscriberService.getById(toUser.getNotificationsEmail()) != null)
//                return;
//            User otherUser = this.userService.getById(isOwner ? dd.getOwnerUserId() : dd.getDateeUserId());
//
//            Map<String, String> context = new HashMap<>();
//            context.put("toName", toUser.getDisplayName());
//            context.put("otherPersonName", otherUser.getDisplayName());
//            context.put("unsubLink", RequestCycle.get().getUrlRenderer().renderFullUrl(RequestCycle.get().mapUrlFor(UnsubscribePage.class, (new PageParameters()).add("q", this.unsubscriberService.createEncryptedUnsubString(toUser.getNotificationsEmail())))));
//
//            String subject = otherUser.getDisplayName() + "'s post-date feedback";
//            boolean wantsMore = isOwner ? dd.isOwnerWantsMore() : dd.isDateeWantsMore();
//            String template = wantsMore ? "date_onSubmittedPostDateFeedback_WantsMore.vm" : "date_onSubmittedPostDateFeedback_NoMore.vm";
//
//            String[] recipients = {toUser.getNotificationsEmail()};
//            this.emailService.sendEmail(template, getServerAbsoluteUrlPrefix(), subject, context, recipients, null);
//        } catch (Exception e) {
//            logger.error("failed onSubmittedPostDateFeedback", e);
//        }
    }

    public void onDateUpdated(DefiniteDate dd, SubmitType type, boolean isOwner) {
//        try {
//            User toUser = this.userService.getById(isOwner ? dd.getDateeUserId() : dd.getOwnerUserId());
//            if (!toUser.isSendNotifications())
//                return;
//            if (this.unsubscriberService.getById(toUser.getNotificationsEmail()) != null)
//                return;
//            User otherUser = this.userService.getById(isOwner ? dd.getOwnerUserId() : dd.getDateeUserId());
//
//            String[] recipients = {toUser.getNotificationsEmail()};
//            Map<String, String> context = new HashMap<>();
//            context.put("toName", toUser.getDisplayName());
//            context.put("otherPersonName", otherUser.getDisplayName());
//            context.put("greeting", dd.getGreetingMsg());
//            context.put("what", dd.getDoingWhat());
//            context.put("whereName", dd.getLocationName());
//            context.put("whereLocation", dd.getLocationAddr());
//            context.put("dateWhen", DateUtil.printDateTime(dd.getDoingWhen(), null));
//            context.put("viewProfileUrl", RequestCycle.get().getUrlRenderer().renderFullUrl(RequestCycle.get().mapUrlFor(ViewProfilePage.class, (new PageParameters()).add("id", Integer.valueOf(otherUser.getId())))));
//            context.put("unsubLink", RequestCycle.get().getUrlRenderer().renderFullUrl(RequestCycle.get().mapUrlFor(UnsubscribePage.class, (new PageParameters()).add("q", this.unsubscriberService.createEncryptedUnsubString(toUser.getNotificationsEmail())))));
//
//            if (type == SubmitType.PROPOSE_NEW_PLAN) {
//                context.put("otherPersonNameMsg", otherUser.getDisplayName() + " has proposed the following date:");
//                String subject = otherUser.getDisplayName() + " has proposed a date";
//                this.emailService.sendEmail("date_onDateUpdate.vm", getServerAbsoluteUrlPrefix(), subject, context, recipients, null);
//            } else if (type == SubmitType.PROPOSE_CHANGE) {
//                context.put("otherPersonNameMsg", otherUser.getDisplayName() + " has proposed the following change to your date:");
//                String subject = otherUser.getDisplayName() + " has proposed a change to your date";
//                this.emailService.sendEmail("date_onDateUpdate.vm", getServerAbsoluteUrlPrefix(), subject, context, recipients, null);
//            } else if (type == SubmitType.ACCEPT) {
//                context.put("otherPersonNameMsg", otherUser.getDisplayName() + " has accepted the following date:");
//                String subject = otherUser.getDisplayName() + " has accepted your date";
//                this.emailService.sendEmail("date_onDateUpdate.vm", getServerAbsoluteUrlPrefix(), subject, context, recipients, null);
//            } else if (type == SubmitType.DECLINE) {
//                String subject = otherUser.getDisplayName() + " has declined your date";
//                this.emailService.sendEmail("date_onDeclineDate.vm", getServerAbsoluteUrlPrefix(), subject, context, recipients, null);
//            }
//
//        } catch (Exception e) {
//            logger.error("failed onSubmittedPostDateFeedback", e);
//        }
    }


    public void sendReminder(DefiniteDate dd, boolean isOwner, boolean feedback) {
//        try {
//            User toUser = this.userService.getById(isOwner ? dd.getDateeUserId() : dd.getOwnerUserId());
//            if (!toUser.isSendNotifications())
//                return;
//            if (this.unsubscriberService.getById(toUser.getNotificationsEmail()) != null)
//                return;
//            User otherUser = this.userService.getById(isOwner ? dd.getOwnerUserId() : dd.getDateeUserId());
//
//            String[] recipients = {toUser.getNotificationsEmail()};
//            Map<String, String> context = new HashMap<>();
//            context.put("toName", toUser.getDisplayName());
//            context.put("otherPersonName", otherUser.getDisplayName());
//            context.put("greeting", dd.getGreetingMsg());
//            context.put("what", dd.getDoingWhat());
//            context.put("whereName", dd.getLocationName());
//            context.put("whereLocation", dd.getLocationAddr());
//            context.put("dateWhen", DateUtil.printDateTime(dd.getDoingWhen(), null));
//            context.put("viewProfileUrl", getServerAbsoluteUrlPrefix() + "viewProfile?id=" + otherUser.getId());
//            context.put("unsubLink", getServerAbsoluteUrlPrefix() + "u?q=" + this.unsubscriberService.createEncryptedUnsubString(toUser.getNotificationsEmail()));
//
//            context.put("instructions", feedback ? "Please provide your feedback for this date:" : "This is only a reminder about your upcoming date, have fun!");
//
//            String subject = "You have a date today with " + otherUser.getDisplayName() + " at " + DateUtil.printTime(dd.getDoingWhen());
//            if (feedback)
//                subject = "Feedback needed for your date with " + otherUser.getDisplayName() + " on " + DateUtil.printDate(dd.getDoingWhen());
//            this.emailService.sendEmail("date_dayOfReminder.vm", getServerAbsoluteUrlPrefix(), subject, context, recipients, null);
//        } catch (Exception e) {
//            logger.error("failed sendReminder", e);
//        }
    }


    public String getServerAbsoluteUrlPrefix() {
        return this.serverAbsoluteUrlPrefix;
    }

    public void setServerAbsoluteUrlPrefix(String serverAbsoluteUrlPrefix) {
        this.serverAbsoluteUrlPrefix = serverAbsoluteUrlPrefix;
    }
}
