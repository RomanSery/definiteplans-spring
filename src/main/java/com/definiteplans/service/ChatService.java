package com.definiteplans.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.definiteplans.controller.model.ActionItem;
import com.definiteplans.controller.model.ChatRow;
import com.definiteplans.dao.ChatMsgRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.ChatMsg;
import com.definiteplans.dom.User;
import com.definiteplans.email.EmailService;
import com.definiteplans.util.DateUtil;

@Service
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final UserService userService;
    private final ChatMsgRepository chatMsgRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public static final int MAX_CHAT_MSGS = 6;

    public ChatService(UserService userService, ChatMsgRepository chatMsgRepository, UserRepository userRepository, EmailService emailService) {
        this.userService = userService;
        this.chatMsgRepository = chatMsgRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public int getNumMsgsRemaining(User currUser, User profile) {
        long numSent = chatMsgRepository.countByFromIdAndToId(currUser.getId(), profile.getId());
        if(numSent > MAX_CHAT_MSGS) {
            return 0;
        }
        return (int) (MAX_CHAT_MSGS - numSent);
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


    public void sendChatMsg(Integer toProfileId, String chatMessage) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return;
        }

        Optional<User> found = userRepository.findById(toProfileId);
        if(found.isEmpty()) {
            return;
        }

        User sendTo = found.get();

        int numRemaining = getNumMsgsRemaining(currUser, sendTo);
        if(numRemaining == 0) {
            return;
        }

        if(!userService.canViewProfile(currUser, sendTo)) {
            return;
        }

        ChatMsg msg = new ChatMsg();
        msg.setFromId(currUser.getId());
        msg.setToId(sendTo.getId());
        msg.setMessage(chatMessage);
        msg.setSentDate(DateUtil.now());
        msg.setIsRead(false);
        msg = chatMsgRepository.save(msg);

        emailService.sendNewChatMsgEmail(msg, sendTo, currUser);
    }


    public List<ActionItem> getMyUnreadMsgs(User currUser) {

        List<ActionItem> toReturn = new ArrayList<>();

        List<ChatMsg> msgs = chatMsgRepository.getMyUnreadChatMsgs(currUser.getId());
        for(ChatMsg msg : msgs) {

            Optional<User> found = userRepository.findById(msg.getFromId());
            if(found.isEmpty()) {
                continue;
            }
            String displayName = found.get().getDisplayName();
            String url = "/profiles/" + msg.getFromId();
            toReturn.add(new ActionItem(url, displayName + " sent you a message!"));
        }

        return toReturn;
    }
}
