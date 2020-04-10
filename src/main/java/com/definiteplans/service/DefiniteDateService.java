package com.definiteplans.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.DateParticipantStatus;
import com.definiteplans.email.EmailService;
import com.definiteplans.util.DateUtil;

@Service
public class DefiniteDateService {
    private final DefiniteDateRepository definiteDateRepository;
    private final EmailService emailService;
    private final ZipCodeRepository zipCodeRepository;

    public DefiniteDateService(DefiniteDateRepository definiteDateRepository, EmailService emailService, ZipCodeRepository zipCodeRepository) {
        this.definiteDateRepository = definiteDateRepository;
        this.emailService = emailService;
        this.zipCodeRepository = zipCodeRepository;
    }

    public DefiniteDate getById(int dateId) {
        if (dateId <= 0) {
            return null;
        }
        Optional<DefiniteDate> found = definiteDateRepository.findById(dateId);
        return found.isPresent() ? found.get() : null;
    }

    public List<DefiniteDate> getPastDatesByOwnerAndDatee(User owner, User datee) {
        List<DefiniteDate> arr = this.definiteDateRepository.getPastDatesByOwnerAndDatee(owner.getId(), datee.getId());
        return (arr != null) ? arr : Collections.emptyList();
    }

    public DefiniteDate getActiveDate(User owner, User datee) {
        return definiteDateRepository.getActiveDate(owner.getId(), datee.getId());
    }

    public List<DefiniteDate> getMyUpcomingDates(User currUser) {
        List<DefiniteDate> arr = definiteDateRepository.getMyUpcomingDates(currUser.getId());
        return (arr != null) ? arr : Collections.emptyList();
    }

    public boolean save(DefiniteDate date) {
        if (date == null) {
            return false;
        }

        definiteDateRepository.save(date);
        return true;
    }

    public DefiniteDate createNew(User currUser) {
        Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
        return new DefiniteDate(zip.isPresent() ? zip.get().getTimezone() : "America/New_York");
    }


    public void sendPostDateFeedbackReminders() {
        List<DefiniteDate> arr = definiteDateRepository.getDatesToSendFeedbackReminderFor();
        for (DefiniteDate dd : arr) {

            try {
                boolean bothApproved = (dd.getOwnerStatusId() == DateParticipantStatus.APPROVED.getId() && dd.getDateeStatusId() == DateParticipantStatus.APPROVED.getId());
                boolean isTooLateToAccept = (dd.getDoingWhen() != null && DateUtil.isInThePast(dd.getDoingWhen(), dd.getTimezone()));
                boolean bothGaveFeedback = (dd.isOwnerGaveFeedback() && dd.isDateeGaveFeedback());

                if (bothApproved && isTooLateToAccept && !bothGaveFeedback) {
                    //if (!dd.isOwnerGaveFeedback()) this.dateLetterManager.sendReminder(dd, true, true);
                    //if (!dd.isDateeGaveFeedback()) this.dateLetterManager.sendReminder(dd, false, true);

                    dd.setPostDateEmailSent(true);
                    definiteDateRepository.save(dd);
                }
            } catch (Exception e) {
                this.emailService.sendAlertMail(String.format("FAILED sendPostDateFeedbackReminders(%s)", dd) + e);
            }
        }
    }

    public void sendDayOfReminders() {
        List<DefiniteDate> arr = definiteDateRepository.getDatesToSendReminderFor();
        for (DefiniteDate dd : arr) {

            try {
                boolean bothApproved = (dd.getOwnerStatusId() == DateParticipantStatus.APPROVED.getId() && dd.getDateeStatusId() == DateParticipantStatus.APPROVED.getId());
                boolean isTooLateToModify = (bothApproved && DateUtil.getHoursBetween(DateUtil.getCurrentServerTime(0, dd.getTimezone()), dd.getDoingWhen(), dd.getTimezone()) <= 12);
                boolean isInThePast = (dd.getDoingWhen() != null && DateUtil.isInThePast(dd.getDoingWhen(), dd.getTimezone()));

                if (isTooLateToModify && !isInThePast) {
                    //this.dateLetterManager.sendReminder(dd, true, false);
                    //this.dateLetterManager.sendReminder(dd, false, false);

                    dd.setEmailReminderSent(true);
                    definiteDateRepository.save(dd);
                }
            } catch (Exception e) {
                this.emailService.sendAlertMail(String.format("FAILED sendDayOfReminders(%s)", dd) + e);
            }
        }
    }
}
