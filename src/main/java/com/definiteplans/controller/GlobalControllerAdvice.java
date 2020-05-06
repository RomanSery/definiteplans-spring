package com.definiteplans.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.definiteplans.controller.model.CurrUserInfo;
import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currUserInfo")
    public CurrUserInfo populateCurrUserInfo() {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new CurrUserInfo();
        }

        CurrUserInfo info = new CurrUserInfo();
        info.setCurrUserDisplayName(currUser.getDisplayName());
        info.setCurrUserImg(userService.getProfileImg(currUser, true));
        info.setCurrUserId(currUser.getId());
        info.setProfileComplete(currUser.isComplete());

        if(!currUser.isComplete()) {
            info.setMissingFields(currUser.getMissingFields());
        }

        return info;
    }


}
