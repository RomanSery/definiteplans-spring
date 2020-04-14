package com.definiteplans.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.email.EmailService;

@Controller
public class LoginController {
    private final EmailService emailService;

    public LoginController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView m = new ModelAndView("login");
        m.addObject("title", "Login");
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
}
