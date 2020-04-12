package com.definiteplans.controller;

import java.time.LocalDateTime;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.dom.User;

@Controller
public class LoginController {

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
    public ModelAndView forgotPassword(String loginemail) {
        

        ModelAndView m = new ModelAndView("forgot_pwd");
        m.addObject("title", "Forgot Password");
        return m;
    }
}
