package com.definiteplans.email;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.definiteplans.dao.UnsubscriberRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.UserTokenRepository;
import com.definiteplans.dom.ChatMsg;
import com.definiteplans.dom.User;
import com.definiteplans.dom.UserToken;
import com.definiteplans.util.DateUtil;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final SmtpService smtpService;
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;
    private final UnsubscriberRepository unsubscriberRepository;

    public EmailService(SmtpService smtpService, UserTokenRepository userTokenRepository, UserRepository userRepository, UnsubscriberRepository unsubscriberRepository) {
        this.smtpService = smtpService;
        this.userTokenRepository = userTokenRepository;
        this.userRepository = userRepository;
        this.unsubscriberRepository = unsubscriberRepository;
    }

    public static String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }



    public void sendEmailValidationEmail(User user) {

        if(user == null) {
            return;
        }

        UserToken token = getToken(user.getId(), null);

        try {
            Map<String, String> context = new HashMap<>();
            context.put("name", user.getDisplayName());

            UriComponents uriComponents = UriComponentsBuilder.fromPath(getBaseUrl() + "/confirmemail")
                    .query("id={tokenId}&uid={uid}&token={token}")
                    .buildAndExpand(String.valueOf(token.getId()), String.valueOf(token.getUserId()), token.getToken());

            String emailValidationUrl = uriComponents.toUriString();
            context.put("emailValidationUrl", emailValidationUrl);

            String subject = "Confirm your email now to activate your account";
            smtpService.sendEmail("confirm_email.fmt", subject, user.getEmail(), context);
        } catch (Exception e) {
            logger.error("failed to send email validation email", e);
        }
    }

    public void sendResetPasswordEmail(String loginEmail) {

        User user = userRepository.findByEmail(loginEmail);
        if(user == null) {
            return;
        }

        UserToken token = getToken(user.getId(), DateUtil.now().plusHours(1));

        try {
            Map<String, String> context = new HashMap<>();

            UriComponents uriComponents = UriComponentsBuilder.fromPath(getBaseUrl() + "/resetpwd")
                    .query("id={tokenId}&uid={uid}&token={token}")
                    .buildAndExpand(String.valueOf(token.getId()), String.valueOf(token.getUserId()), token.getToken());

            String resetPasswordUrl = uriComponents.toUriString();
            context.put("resetPasswordUrl", resetPasswordUrl);

            String subject = "Reset your password";
            smtpService.sendEmail("reset_pwd.fmt", subject, user.getEmail(), context);
        } catch (Exception e) {
            logger.error("failed to send reset pwd email", e);
        }
    }


    public void sendNewChatMsgEmail(ChatMsg msg, User sentTo, User sentFrom) {

        if(msg == null || sentTo == null || sentFrom == null) {
            return;
        }

        if(unsubscriberRepository.findById(sentTo.getEmail()).isPresent()) {
            logger.info("skipping email due to unsub: {}", sentTo.getEmail());
            return;
        }

        UserToken token = getToken(sentTo.getId(), null);

        try {
            Map<String, String> context = new HashMap<>();
            context.put("name", sentTo.getDisplayName());
            context.put("fromName", sentFrom.getDisplayName());

            UriComponents uriComponents = UriComponentsBuilder.fromPath(getBaseUrl() + "/profiles/" + sentFrom.getId()).build();
            context.put("viewProfileUrl", uriComponents.toUriString());
            context.put("unsubLink", getUnsubUrl(token));

            String subject = sentFrom.getDisplayName() + " sent you a message!";
            smtpService.sendEmail("new_msg.fmt", subject, sentTo.getEmail(), context);
        } catch (Exception e) {
            logger.error("failed sendNewChatMsgEmail", e);
        }
    }


    public void sendEmail(String template, Map<String, String> context, String subject, User sendTo) {
        if(sendTo == null || StringUtils.isBlank(template) || StringUtils.isBlank(subject)) {
            return;
        }

        String toEmail = sendTo.getEmail();
        if(unsubscriberRepository.findById(toEmail).isPresent()) {
            logger.info("skipping email due to unsub: {}", toEmail);
            return;
        }

        try {
            UserToken token = getToken(sendTo.getId(), null);
            context.put("unsubLink", getUnsubUrl(token));
            smtpService.sendEmail(template, subject, toEmail, context);
        } catch (Exception e) {
            logger.error("failed sendEmail template={}, subject={}", template, subject, e);
            e.printStackTrace();
        }

    }


    private UserToken getToken(int userId, LocalDateTime expiration) {
        UserToken token = new UserToken();
        token.setUserId(userId);
        token.setCreationDate(DateUtil.now());
        token.setExpiration(expiration);
        token.setToken(UUID.randomUUID().toString());
        token = userTokenRepository.save(token);
        return token;
    }

    private String getUnsubUrl(UserToken token) {
        UriComponents unsubLink = UriComponentsBuilder.fromPath(getBaseUrl() + "/unsub")
                .query("id={tokenId}&uid={uid}&token={token}")
                .buildAndExpand(String.valueOf(token.getId()), String.valueOf(token.getUserId()), token.getToken());
        return unsubLink.toUriString();
    }
}
