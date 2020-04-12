package com.definiteplans.email;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.definiteplans.dom.User;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final SmtpService smtpService;

    public EmailService(SmtpService smtpService) {
        this.smtpService = smtpService;
    }

    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }



    public void sendEmailValidationLetter(User myUser) {
        try {
            Map<String, String> context = new HashMap<>();
            context.put("name", myUser.getDisplayName());


            String validationString = "";//this.userService.createEncryptedExpiringUserValidationString(myUser);

            //Url relativeUrl = RequestCycle.get().mapUrlFor(ValidateEmailPage.class, (new PageParameters()).add("q", validationString));
            context.put("emailValidationUrl", validationString);

            String subject = "Confirm your email now to activate your account";
            smtpService.sendEmail("confirm_email.fmt", subject, myUser.getEmail(), context);
        } catch (Exception e) {
            logger.error("failed to send email validation letter", e);
        }
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
//        try {
//            Map<String, String> context = new HashMap<>();
//            context.put("date", DateUtil.printDateTime(DateUtil.getCurrentServerTime(), null));
//            context.put("userId", (userId != null) ? String.valueOf(userId) : "Not-Logged in");
//            context.put("url", url);
//            context.put("msg", msg);
//
//            String[] recipients = {this.emailService.getBugSupportEmail()};
//            this.emailService.sendEmail("emailFeedback_template.vm", getServerAbsoluteUrlPrefix(), "Feedback form submission", context, recipients, null);
//        } catch (Exception e) {
//
//            logger.error("failed to send sendFeedback", e);
//        }
    }

}
