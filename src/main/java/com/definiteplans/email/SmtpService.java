package com.definiteplans.email;

import java.time.LocalDateTime;
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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;


@Service
public class SmtpService {
    private static final Logger logger = LoggerFactory.getLogger(SmtpService.class);

    private static final String from = "noreply@letsdefinitly.com";

    private final JavaMailSender sender;
    private final SentEmailRepository sentEmailRepository;
    private final Configuration freeMarkerConfiguration;

    public SmtpService(JavaMailSender sender, SentEmailRepository sentEmailRepository, Configuration freeMarkerConfiguration) {
        this.sender = sender;
        this.sentEmailRepository = sentEmailRepository;
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }


    public void sendEmail(String templateFile, String subject, String to, Map<String, String> ctx) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String body = doTemplatingForContent(templateFile, ctx);

        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText(body, true);

        sender.send(message);

        SentEmail email = new SentEmail(subject, body, from, to);
        email.setEmailSentDate(LocalDateTime.now());
        sentEmailRepository.save(email);
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
