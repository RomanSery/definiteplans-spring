package com.definiteplans.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.definiteplans.controller.model.CurrUserInfo;
import com.definiteplans.dao.ChatMsgRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final UserService userService;
    private final ChatMsgRepository chatMsgRepository;
    private final UserRepository userRepository;

    public GlobalControllerAdvice(UserService userService, ChatMsgRepository chatMsgRepository, UserRepository userRepository) {
        this.userService = userService;
        this.chatMsgRepository = chatMsgRepository;
        this.userRepository = userRepository;
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
        info.setNumUnreadMsgs(chatMsgRepository.getNumUnreadChatMsgs(currUser.getId()));

        if(!currUser.isComplete()) {
            info.setMissingFields(currUser.getMissingFields());
        }

        currUser.setLastLoginDate(DateUtil.now());
        userRepository.save(currUser);

        return info;
    }


}
