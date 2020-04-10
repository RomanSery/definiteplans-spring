package com.definiteplans.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.User;
import com.definiteplans.service.DefiniteDateService;
import com.definiteplans.service.UserService;

@Controller
public class ViewProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EnumValueRepository enumValueRepository;
    private final DefiniteDateService definiteDateService;

    public ViewProfileController(UserService userService, UserRepository userRepository, EnumValueRepository enumValueRepository, DefiniteDateService definiteDateService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.enumValueRepository = enumValueRepository;
        this.definiteDateService = definiteDateService;
    }

    @GetMapping("/profiles/")
    public ModelAndView viewProfile() {
        return new ModelAndView(new RedirectView("/browse"));
    }


    @GetMapping("/profiles/{userId}")
    public ModelAndView viewProfile(@PathVariable("userId") Integer userId) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }


        Optional<User> found = userRepository.findById(userId);
        if(found.isEmpty()) {
            return new ModelAndView(new RedirectView("/browse"));
        }

        User profile = found.get();
        if(!userService.canViewProfile(currUser, profile)) {
            return new ModelAndView(new RedirectView("/browse"));
        }




        ModelAndView m = new ModelAndView("view_profile");
        return m;
    }


}
