package com.coderdreams.definiteplans.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.coderdreams.definiteplans.dom.User;
import com.coderdreams.definiteplans.service.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currUserDisplayName")
    public String populateUserDisplayName() {
        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            return currUser.getDisplayName();
        }
        return null;
    }

    @ModelAttribute("currUserId")
    public int populateCurrUserId() {
        return userService.getCurrentUserId();
    }

}
