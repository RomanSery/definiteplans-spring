package com.definiteplans.email;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private String from;

    private String bugSupportEmail;
    private String bccAuditEmail;


    public void sendAlertMail(String alert) {
//        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage(this.alertMailMessage);
//            mailMessage.setText(alert);
//            this.mailSender.send(mailMessage);
//        } catch (Exception e) {
//        }
    }


    public void sendEmail(String template, String serverAbsoluteUrlPrefix, String subject, Map<String, String> context, String[] toRecipients, String[] ccRecipients) throws Exception {
        String contents = doTemplating(template, context);
//        SimpleMailMessage msg = new SimpleMailMessage();
//
//        msg.setTo(toRecipients);
//        if (ccRecipients != null) msg.setCc(ccRecipients);
//        msg.setSubject(subject);
//        msg.setFrom(this.from);
//        msg.setText(contents);
//        msg.setBcc(getBccAuditEmail());

//        try {
//            this.mailSender.send(msg);
//        } catch (Exception e) {
//            logger.debug("failed message text: {}", contents);
//            throw e;
//        }
    }


    public void sendHtmlEmail(String template, String serverAbsoluteUrlPrefix, String subject, Map<String, String> context, String[] toRecipients, String[] ccRecipients) throws Exception {
        String middleContent = doTemplating(template, context);

        context.put("urlPrefix", serverAbsoluteUrlPrefix + "images");
        context.put("middleContent", middleContent);

//        MimeMessage message = this.mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//        String htmlContent = doTemplating("transactional.build.html", context);
//
//
//        helper.setTo(toRecipients);
//        if (ccRecipients != null) helper.setCc(ccRecipients);
//        helper.setSubject(subject);
//        helper.setFrom(this.from);
//        helper.setText(htmlContent, true);
//        helper.setBcc(getBccAuditEmail());
//
//        try {
//            this.mailSender.send(message);
//        } catch (Exception e) {
//            logger.debug("failed message text: {}", htmlContent);
//            throw e;
//        }
    }


    String doTemplating(String content, Map<String, String> ctx) throws Exception {

//        Configuration cfg = freeMarkerConfiguration;
//        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
//        try {
//            Template temp = new Template("emailTemplate", new StringReader(content), cfg);
//            StringWriter aWriter = new StringWriter();
//            temp.process(ctx, aWriter);
//            return aWriter.toString();
//        } catch (Exception e) {
//            logger.error("doTemplating {}", e.getMessage());
//
//        }
        return "";
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBugSupportEmail() {
        return this.bugSupportEmail;
    }

    public void setBugSupportEmail(String bugSupportEmail) {
        this.bugSupportEmail = bugSupportEmail;
    }

    public String getBccAuditEmail() {
        return this.bccAuditEmail;
    }

    public void setBccAuditEmail(String bccAuditEmail) {
        this.bccAuditEmail = bccAuditEmail;
    }

}
