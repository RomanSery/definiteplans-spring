package com.definiteplans.controller;

import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.controller.model.PwdUpdate;
import com.definiteplans.dao.UnsubscriberRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.Unsubscriber;
import com.definiteplans.dom.User;
import com.definiteplans.dom.enumerations.LoginErrorType;
import com.definiteplans.dom.enumerations.UserStatus;
import com.definiteplans.email.EmailService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;

@Controller
public class LoginController {
    private final EmailService emailService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UnsubscriberRepository unsubscriberRepository;

    public LoginController(EmailService emailService, UserService userService, UserRepository userRepository, UnsubscriberRepository unsubscriberRepository) {
        this.emailService = emailService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.unsubscriberRepository = unsubscriberRepository;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false, name = "reset") Integer reset,
                              @RequestParam(required = false, name = "confirmed") Integer confirmed,
                              @RequestParam(required = false, name = "email") String email,
                              @RequestParam(required = false, name = "deleted") Integer deleted,
                              @RequestParam(required = false, name = "loginerror") Integer loginerror) {

        ModelAndView m = new ModelAndView("login_new");

        if(loginerror != null) {
            LoginErrorType type = LoginErrorType.getById(loginerror);
            if(type != null) {
                m.addObject("login_error_type", type.getId());
                m.addObject("login_error", type.getDescription());
            }
        }

        m.addObject("title", "Login");
        m.addObject("email", email);
        m.addObject("was_pwd_reset", reset != null && reset == 1);
        m.addObject("was_confirmed", confirmed != null && confirmed == 1);
        m.addObject("was_deleted", deleted != null && deleted == 1);

        m.addObject("fbLoginUrl", OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/facebook");
        m.addObject("googleLoginUrl", OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/google");
        return m;
    }

    @GetMapping("/forgotpwd")
    public ModelAndView forgotPassword() {
        ModelAndView m = new ModelAndView("forgot_pwd");
        m.addObject("title", "Forgot Password");
        return m;
    }

    @PostMapping("/forgotpwd")
    public ModelAndView forgotPassword(@RequestParam("loginemail") String loginEmail) {
        emailService.sendResetPasswordEmail(loginEmail);
        return new ModelAndView(new RedirectView("/forgotpwdthanks"), Map.of("email", loginEmail));
    }

    @GetMapping("/forgotpwdthanks")
    public ModelAndView forgotPasswordThanks(Model model, @RequestParam String email) {
        ModelAndView m = new ModelAndView("forgot_pwd_confirm");
        m.addObject("title", "Thanks - Email Sent");
        m.addObject("email", email);
        return m;
    }

    @GetMapping("/resetpwd")
    public ModelAndView resetPassword(@RequestParam("id") Integer id, @RequestParam("uid") Integer uid, @RequestParam("token") String token) {
        if(userService.getUserIdFromToken(id, uid, token) == null) {
            ModelAndView m = new ModelAndView("invalid_link");
            m.addObject("title", "Invalid Link");
            return m;
        }

        ModelAndView m = new ModelAndView("reset_pwd");
        m.addObject("title", "Reset my Password");
        m.addObject("user", new PwdUpdate(id, uid, token));
        return m;
    }

    @PostMapping("/resetpwd")
    public ModelAndView resetPassword(@ModelAttribute("user") @Valid PwdUpdate pwd, BindingResult bindingResult) {
        Integer userId = userService.getUserIdFromToken(pwd.getTokenId(), pwd.getTokenUserId(), pwd.getToken());
        if(userId == null) {
            return new ModelAndView(new RedirectView("/login"));
        }

        ProfileValidator.validate(pwd, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("reset_pwd", Map.of("user", pwd));
        }

        Optional<User> u = userRepository.findById(userId);
        if(u.isPresent()) {
            boolean success = userService.resetForgottenPwd(u.get(), pwd);
            if (!success) {
                return new ModelAndView("reset_pwd", Map.of("user", pwd));
            }
        }

        return new ModelAndView(new RedirectView("/login?reset=1"));
    }


    @GetMapping("/confirmemail")
    public ModelAndView confirmEmail(@RequestParam("id") Integer id, @RequestParam("uid") Integer uid, @RequestParam("token") String token) {
        Integer userId = userService.getUserIdFromToken(id, uid, token);
        if(userId == null) {
            ModelAndView m = new ModelAndView("invalid_link");
            m.addObject("title", "Invalid Link");
            return m;
        }

        Optional<User> found = userRepository.findById(userId);
        if(found.isPresent()) {
            User u = found.get();
            if(u.getUserStatus() != UserStatus.ACTIVE.getId()) {
                u.setUserStatus(UserStatus.ACTIVE.getId());
                userService.saveUser(u, true);
            }
        }

        return new ModelAndView(new RedirectView("/login?confirmed=1"));
    }


    @GetMapping("/resendValidationEmail")
    public @ResponseBody AjaxResponse resendValidationEmail(@RequestParam("email") String email) {
        userService.resendValidationEmail(email);
        return AjaxResponse.success("Success");
    }


    @GetMapping("/unsub")
    public ModelAndView unsubscribe(@RequestParam("id") Integer id, @RequestParam("uid") Integer uid,
                                    @RequestParam("token") String token) {
        Integer userId = userService.getUserIdFromToken(id, uid, token);
        if(userId == null) {
            ModelAndView m = new ModelAndView("invalid_link");
            m.addObject("title", "Invalid Link");
            return m;
        }

        Optional<User> found = userRepository.findById(userId);
        if(found.isPresent()) {
            User u = found.get();
            Unsubscriber unsub = new Unsubscriber();
            unsub.setEmail(u.getEmail());
            unsub.setUnsubDate(DateUtil.now());
            unsubscriberRepository.save(unsub);
        }



        ModelAndView m = new ModelAndView("unsubscribed");
        m.addObject("title", "Successfully Unsubscribed");
        return m;
    }


    private static class ProfileValidator {
        private static void validate(PwdUpdate obj, Errors e) {
            ValidationUtils.rejectIfEmpty(e, "password1", "", "New password is required");
            ValidationUtils.rejectIfEmpty(e, "password2", "", "Confirm new password");

            if(!obj.getPassword1().equals(obj.getPassword2())) {
                e.reject("pwd.mismatch", "Passwords must match");
            }
        }
    }
}
