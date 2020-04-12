package com.definiteplans.email;

import java.time.LocalDateTime;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.SentEmailRepository;
import com.definiteplans.dom.SentEmail;


@Service
public class SmtpService {
    private static final Logger logger = LoggerFactory.getLogger(SmtpService.class);

    private static final String from = "noreply@letsdefinitly.com";

    private final JavaMailSender sender;
    private final SentEmailRepository sentEmailRepository;


    public SmtpService(JavaMailSender sender, SentEmailRepository sentEmailRepository) {
        this.sender = sender;
        this.sentEmailRepository = sentEmailRepository;
    }


    public void sendEmail(String subject, String to, String body) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText(body, true);

        sender.send(message);

        SentEmail email = new SentEmail(subject, body, from, to);
        email.setEmailSentDate(LocalDateTime.now());
        sentEmailRepository.save(email);
    }




}
