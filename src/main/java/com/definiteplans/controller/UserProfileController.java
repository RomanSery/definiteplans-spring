package com.definiteplans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;

@Controller
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/profile/{userId}")
    public ModelAndView viewUserProfile(Model model, @PathVariable int userId) {
        User user = userService.getUser(userId);
        if(user == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView mv = new ModelAndView("user_profile");
        mv.addObject("displayName", user.getDisplayName());
        return mv;
    }

}
