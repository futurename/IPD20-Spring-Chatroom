package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.ChatMessage;
import com.chatroom.ipd20.models.FavChannel;
import com.chatroom.ipd20.models.UserConnectInfo;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.BlobService;
import com.chatroom.ipd20.services.ChannelRepository;
import com.chatroom.ipd20.services.MessageRepository;
import com.chatroom.ipd20.services.UserRepository;
import org.jboss.logging.annotations.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/29
 **/

@Controller
public class ChatController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    UserRepository userRepository;

    private SimpMessagingTemplate msgTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate template){
        this.msgTemplate = template;
    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage chatMessage) {
        /*
         * FixMe: simulate saving message sent by user 2
         */
        LocalDateTime createdTS = LocalDateTime.now();
        Message newMsg = new Message(0, chatMessage.getBody(), null,
                new Channel(chatMessage.getChannelId()), new User(chatMessage.getSenderId()), createdTS);

        messageRepository.save(newMsg);

        String senderName = userRepository.findById(chatMessage.getSenderId()).get().getName();

        chatMessage.setCreatedTS(createdTS);
        chatMessage.setSenderName(senderName);

        msgTemplate.convertAndSend("/chatroom/"+chatMessage.getChannelId(), chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@RequestBody UserConnectInfo userConnectInfo) {
        // Add username in web socket session
        //headerAccessor.getSessionAttributes().put("username", userConnectInfo.getSenderName());
        LocalDateTime createdTS =LocalDateTime.now();
        String connString = "<span class='font-bold text-red-500 text-base'>" + userConnectInfo.getSenderName() + "</span> has " +
                "joined the chatroom...\uD83D\uDE0A";
        Message connMsg = new Message(0, connString, null, new Channel(userConnectInfo.getSenderId()),
                new User(userConnectInfo.getSenderId()), createdTS);
        messageRepository.save(connMsg);
        ChatMessage connChatMsg = new ChatMessage();
        connChatMsg.setSenderName(userConnectInfo.getSenderName());
        connChatMsg.setType(ChatMessage.MessageType.JOIN);
        connChatMsg.setBody(connString);
        connChatMsg.setSenderId(userConnectInfo.getSenderId());
        connChatMsg.setCreatedTS(createdTS);
        connChatMsg.setChannelId(userConnectInfo.getChannelId());
        msgTemplate.convertAndSend("/chatroom/"+ connChatMsg.getChannelId(), connChatMsg);

    }

    @RequestMapping(value = {"/index", "/"})
    public String getAllMessages(Model model) {
        model.addAttribute("allChannels", channelRepository.findAll());

        /*
           FIX ME, USING user 1 as default user to retrieve fav channels.
         */
        User user = userRepository.findById(1).orElse(null);
        Set<Channel> favChannels = user.getFavoriteChannels();
        model.addAttribute("allFavChannels",favChannels);
        return "index";
    }

    @PostMapping("/chatroom/addFavChannel")
    public void addFavChannel(@RequestBody FavChannel favChannel){
        User user = userRepository.findById(favChannel.getUserId()).orElse(null);
        Set<Channel> favChannels = user.getFavoriteChannels();
        Channel channel = new Channel();
        channel.setId(favChannel.getChannelId());
        favChannels.add(channel);
        userRepository.save(user);
    }

    @DeleteMapping("/chatroom/delFavChannel")
    public void deleteFavChannel(@RequestBody FavChannel favChannel){
        User user = userRepository.findById(favChannel.getUserId()).orElse(null);
        Set<Channel> favChannels = user.getFavoriteChannels();
        favChannels.removeIf(a->a.getId()==favChannel.getChannelId());
        userRepository.save(user);
    }


}