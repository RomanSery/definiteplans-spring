package com.definiteplans.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.definiteplans.controller.model.ChatRow;
import com.definiteplans.dao.ChatMsgRepository;
import com.definiteplans.dom.ChatMsg;
import com.definiteplans.dom.User;
import com.definiteplans.util.DateUtil;

@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final UserService userService;
    private final ChatMsgRepository chatMsgRepository;

    public ChatService(UserService userService, ChatMsgRepository chatMsgRepository) {
        this.userService = userService;
        this.chatMsgRepository = chatMsgRepository;
    }


    public List<ChatRow> getChatMsgs(User currUser, User profile) {

        List<ChatMsg> arr = chatMsgRepository.getChat(currUser.getId(), profile.getId());
        List<ChatRow> msgs = new ArrayList<>();

        for(ChatMsg msg : arr) {

            boolean isFromMe = msg.getFromId() == currUser.getId();
            int userId = msg.getFromId();
            String displayName = isFromMe ? "Me" : profile.getDisplayName();
            String img = isFromMe ? userService.getProfileImg(currUser, true) : userService.getProfileImg(profile, true);
            String content = msg.getMessage();
            String date = DateUtil.printDateTime(msg.getSentDate());

            msgs.add(new ChatRow(userId, displayName, img, content, date));
        }

        return msgs;
    }


}
