package com.definiteplans.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;

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

    @ModelAttribute("currUserImg")
    public String populateUserImg() {
        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            String img = "<a href=\"viewProfile?id={currUserId}\"><img class=\"img-circle\" width=\"48\" src=\"{imgSrc}\" /></a>";
            img = img.replace("{currUserId}", String.valueOf(currUser.getId()));
            img = img.replace("{imgSrc}", userService.getCurrUserProfileImg());
            return img;
        }
        return null;
    }

    @ModelAttribute("currUserId")
    public int populateCurrUserId() {
        return userService.getCurrentUserId();
    }

}
