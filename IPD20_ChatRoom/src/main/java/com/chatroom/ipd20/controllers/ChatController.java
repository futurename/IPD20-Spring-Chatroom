package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.ChatMessage;
import com.chatroom.ipd20.services.ChannelRepository;
import com.chatroom.ipd20.services.MessageRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/29
 **/

@Controller
public class ChatController {

    @Autowired
    MessageRespository messageRespository;

    @Autowired
    ChannelRepository channelRepository;


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {

        /*
         * FixMe: simulate saving message sent by user 2
         */
        Message newMsg = new Message(0, chatMessage.getBody(), null,
                new Channel(chatMessage.getChannelId()), new User(chatMessage.getSenderId()), LocalDateTime.now());

        //Message newMsg = new Message(0, "direct saving test",null,new Channel(4),new User(2),LocalDateTime.now());
        messageRespository.save(newMsg);

        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", "temSender");
        return chatMessage;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/send/{msg}")
    @ResponseBody
    public Message sendMessage(@PathVariable String msg) {

        Message newMsg = new Message(1, msg);
        messageRespository.save(newMsg);
        return newMsg;
    }

    @RequestMapping(value = {"/index", "/"})
    public String getAllMessages(Model model) {
        model.addAttribute("allChannels", channelRepository.findAll());
        return "index";
    }


}