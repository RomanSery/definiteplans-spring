package com.definiteplans.email;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.UserTokenRepository;
import com.definiteplans.dom.User;
import com.definiteplans.dom.UserToken;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final SmtpService smtpService;
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    public EmailService(SmtpService smtpService, UserTokenRepository userTokenRepository, UserRepository userRepository) {
        this.smtpService = smtpService;
        this.userTokenRepository = userTokenRepository;
        this.userRepository = userRepository;
    }

    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }



    public void sendEmailValidationEmail(User user) {

        if(user == null) {
            return;
        }

        UserToken token = new UserToken();
        token.setUserId(user.getId());
        token.setCreationDate(LocalDateTime.now());
        token.setToken(UUID.randomUUID().toString());
        token = userTokenRepository.save(token);

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

        UserToken token = new UserToken();
        token.setUserId(user.getId());
        token.setCreationDate(LocalDateTime.now());
        token.setExpiration(LocalDateTime.now().plusHours(1));
        token.setToken(UUID.randomUUID().toString());
        token = userTokenRepository.save(token);

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

}
