package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.ChatMessage;
import com.chatroom.ipd20.models.FavChannel;
import com.chatroom.ipd20.models.ConnChannel;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.ChannelRepository;
import com.chatroom.ipd20.services.MessageRepository;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ChatController(SimpMessagingTemplate template) {
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

        msgTemplate.convertAndSend("/chatroom/" + chatMessage.getChannelId(), chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@RequestBody ConnChannel connChannel, Authentication authentication) {

        LocalDateTime createdTS = LocalDateTime.now();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findById(customUserDetails.getId()).get();

        String senderName = user.getName();
        int userId = user.getId();
        String connString = "<span class='font-bold text-red-500 text-base'>" + senderName + "</span> has " +
                "joined the chatroom...\uD83D\uDE0A";

        ChatMessage connChatMsg = new ChatMessage();
        connChatMsg.setSenderName(senderName);
        connChatMsg.setType(ChatMessage.MessageType.JOIN);
        connChatMsg.setBody(connString);
        connChatMsg.setSenderId(userId);
        connChatMsg.setCreatedTS(createdTS);
        connChatMsg.setChannelId(connChannel.getChannelId());
        msgTemplate.convertAndSend("/chatroom/" + connChatMsg.getChannelId(), connChatMsg);

    }

    @RequestMapping(value = {"/index", "/"})
    public String getAllMessages(Model model, HttpServletRequest request, Authentication authentication) {
        model.addAttribute("allChannels", channelRepository.findAll());

        String sessionId = request.getRequestedSessionId();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getId();

        User user = userRepository.findById(userId).orElse(null);
        Set<Channel> favChannels = user.getFavoriteChannels();
        model.addAttribute("allFavChannels", favChannels);
        model.addAttribute("userId",userId);
        model.addAttribute("activeChannels", user.getActiveChannels());

        return "index";
    }

    @PostMapping("/chatroom/addFavChannel")
    @ResponseBody
    public void addFavChannel(@RequestBody FavChannel favChannel) {
        User user = userRepository.findById(favChannel.getUserId()).orElse(null);
        Set<Channel> favChannels = user.getFavoriteChannels();
        Channel channel = new Channel();
        channel.setId(favChannel.getChannelId());
        favChannels.add(channel);
        userRepository.save(user);
    }

    @DeleteMapping("/chatroom/delFavChannel")
    @ResponseBody
    public void deleteFavChannel(@RequestBody FavChannel favChannel) {
        User user = userRepository.findById(favChannel.getUserId()).orElse(null);
        Set<Channel> favChannels = user.getFavoriteChannels();
        favChannels.removeIf(a -> a.getId() == favChannel.getChannelId());
        userRepository.save(user);
    }

    @GetMapping("/chatroom/leave/{channelId}")
    public String leaveChatRoom(Authentication authentication, @PathVariable int channelId, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getId();

        User user = userRepository.findById(userId).get();

        Set<Channel> allActiveChannels = user.getActiveChannels();
        allActiveChannels.removeIf(a -> a.getId() == channelId);
        userRepository.save(user);
        model.addAttribute("allChannels", channelRepository.findAll());
        Set<Channel> favChannels = user.getFavoriteChannels();
        model.addAttribute("allFavChannels", favChannels);
        model.addAttribute("userId",userId);
        model.addAttribute("activeChannels", user.getActiveChannels());
        return "index";
    }


}