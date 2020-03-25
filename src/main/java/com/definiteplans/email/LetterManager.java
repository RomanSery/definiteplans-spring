package com.definiteplans.email;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;

@Service
public class LetterManager {
    private static final Logger logger = LoggerFactory.getLogger(LetterManager.class);
    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;
    private String serverAbsoluteUrlPrefix = null;


    public void sendEmailValidationLetter(User myUser) {
//        try {
//            Map<String, String> context = new HashMap<>();
//            context.put("name", myUser.getDisplayName());
//
//            String validationString = this.userService.createEncryptedExpiringUserValidationString(myUser);
//
//            Url relativeUrl = RequestCycle.get().mapUrlFor(ValidateEmailPage.class, (new PageParameters()).add("q", validationString));
//            context.put("emailValidationUrl", RequestCycle.get().getUrlRenderer().renderFullUrl(relativeUrl));
//
//            String[] recipients = {myUser.getEmail()};
//            String subject = "Confirm your email now to activate your account";
//            this.emailService.sendEmail("emailConfirmation_template.vm", getServerAbsoluteUrlPrefix(), subject, context, recipients, null);
//        } catch (Exception e) {
//
//            logger.error("failed to send email validation letter", e);
//        }
    }

    public void sendEmailResetPasswordLetter(User myUser) {
//        try {
//            Map<String, String> context = new HashMap<>();
//            context.put("name", myUser.getDisplayName());
//            String validationString = this.userService.createEncryptedExpiringUserValidationString(myUser);
//
//            Url relativeUrl = RequestCycle.get().mapUrlFor(ResetPasswordPage.class, (new PageParameters()).add("q", validationString));
//            context.put("resetPasswordUrl", RequestCycle.get().getUrlRenderer().renderFullUrl(relativeUrl));
//
//            String[] recipients = {myUser.getEmail()};
//            String subject = Application.get().getResourceSettings().getLocalizer().getString("resetPassword.subject", null, "Reset your password");
//
//            this.emailService.sendEmail("emailResetPassword_template.vm", getServerAbsoluteUrlPrefix(), subject, context, recipients, null);
//        } catch (Exception e) {
//
//            logger.error("failed to send reset password letter", e);
//        }
    }

    public void sendFeedback(Integer userId, String url, String msg) {
        try {
            Map<String, String> context = new HashMap<>();
            context.put("date", DateUtil.printDateTime(DateUtil.getCurrentServerTime(), null));
            context.put("userId", (userId != null) ? String.valueOf(userId) : "Not-Logged in");
            context.put("url", url);
            context.put("msg", msg);

            String[] recipients = {this.emailService.getBugSupportEmail()};
            this.emailService.sendEmail("emailFeedback_template.vm", getServerAbsoluteUrlPrefix(), "Feedback form submission", context, recipients, null);
        } catch (Exception e) {

            logger.error("failed to send sendFeedback", e);
        }
    }


    public String getServerAbsoluteUrlPrefix() {
        return this.serverAbsoluteUrlPrefix;
    }

    public void setServerAbsoluteUrlPrefix(String serverAbsoluteUrlPrefix) {
        this.serverAbsoluteUrlPrefix = serverAbsoluteUrlPrefix;
    }
}
