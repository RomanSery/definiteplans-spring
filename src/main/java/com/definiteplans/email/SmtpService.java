package com.definiteplans.email;

import java.util.Map;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.definiteplans.dao.SentEmailRepository;
import com.definiteplans.dom.SentEmail;
import com.definiteplans.util.DateUtil;
import com.definiteplans.util.Utils;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;


@Service
public class SmtpService {
    private static final Logger logger = LoggerFactory.getLogger(SmtpService.class);

    private static final String fromEmail = "roman@letsdefinitely.com";

    private final JavaMailSender sender;
    private final SentEmailRepository sentEmailRepository;
    private final Configuration freeMarkerConfiguration;

    private SendGrid sendGrid;

    public SmtpService(JavaMailSender sender, SentEmailRepository sentEmailRepository, Configuration freeMarkerConfiguration) {
        this.sender = sender;
        this.sentEmailRepository = sentEmailRepository;
        this.freeMarkerConfiguration = freeMarkerConfiguration;

        String apiKey = Utils.getVariable("SENDGRID_API_KEY");
        if(!StringUtils.isBlank(apiKey)) {
            sendGrid = new SendGrid(apiKey);
        }
    }


    public void sendEmail(String templateFile, String subject, String to, Map<String, String> ctx) throws Exception {

        String body = doTemplatingForContent(templateFile, ctx);

        if(sendGrid != null) {
            sendEmailWithSendGrid(body, subject, to);
        } else {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(body, false);
            sender.send(message);
        }

        SentEmail email = new SentEmail(subject, body, fromEmail, to);
        email.setEmailSentDate(DateUtil.now());
        sentEmailRepository.save(email);
    }

    private void sendEmailWithSendGrid(String body, String subject, String to) throws Exception {

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        logger.info("sent email: code: {} body: {}", response.getStatusCode(), response.getBody());
    }


    private String doTemplatingForContent(String templateFile, Map<String,?> ctx) {
        if(StringUtils.isBlank(templateFile)) return "";

        Configuration cfg = freeMarkerConfiguration;
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        try {
            Template template = cfg.getTemplate("email\\" + templateFile);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, ctx);
        } catch (Exception e) {
            logger.error("doTemplatingForContent {}", e.getMessage());

        }
        return "";
    }



}
