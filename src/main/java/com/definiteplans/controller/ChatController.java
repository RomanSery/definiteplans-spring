package com.definiteplans.controller;

import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.User;
import com.definiteplans.service.ChatService;
import com.definiteplans.service.UserService;

@Controller
public class ChatController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ChatService chatService;

    public ChatController(UserRepository userRepository, UserService userService, ChatService chatService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @PostMapping("/chat/send")
    public @ResponseBody
    AjaxResponse sendChatMsg(@ModelAttribute("chatMessage") @Valid String chatMessage, @ModelAttribute("profileId") Integer profileId,
                             BindingResult bindingResult) {
        MsgValidator.validate(chatMessage, profileId, bindingResult);
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        }

        userService.sendChatMsg(profileId, chatMessage);
        return AjaxResponse.success("Sent");
    }

    @RequestMapping("/refresh-chat-thread/{userId}")
    public String refreshChatThread(ModelMap m, @PathVariable("userId") Integer userId) {
        User currUser = userService.getCurrentUser();
        Optional<User> found = userRepository.findById(userId);
        if(currUser == null || found.isEmpty()) {
            return "";
        }
        User profile = found.get();
        m.addAttribute("profile", profile);
        m.addAttribute("chat_thread", chatService.getChatMsgs(currUser, profile));
        return "view_profile/profile_frags :: chat";
    }


    private static class MsgValidator {
        private static void validate(String chatMessage, Integer profileId, Errors e) {
            if(StringUtils.isBlank(chatMessage) || chatMessage.length() > 500) {
                e.reject("chatMessage", "Invalid chat message.");
            }
            if(profileId == null) {
                e.reject("chatMessage", "Invalid chat message.");
            }
        }
    }
}
