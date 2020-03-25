package com.definiteplans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;

@Controller
public class HomeController {
    private final UserService userService;
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView homePage(){
        User currUser = userService.getCurrentUser();

        ModelAndView m = new ModelAndView("home");
        return m;
    }
}
