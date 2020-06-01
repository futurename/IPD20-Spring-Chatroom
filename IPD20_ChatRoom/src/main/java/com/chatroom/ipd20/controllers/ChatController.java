package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.ChatMessage;
import com.chatroom.ipd20.services.ChannelRepository;
import com.chatroom.ipd20.services.MessageRespository;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @Autowired
    UserRepository userRepository;

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if(principal != null) {
            model.addAttribute("user", principal.getName());
        }
    }

    private SimpMessagingTemplate msgTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate template){
        this.msgTemplate = template;
    }

    @MessageMapping("/chat.sendMessage")
  //  @SendTo("/topic/{channelId}")
    public void sendMessage(ChatMessage chatMessage) {

        /*
         * FixMe: simulate saving message sent by user 2
         */
        LocalDateTime createdTS = LocalDateTime.now();
        Message newMsg = new Message(0, chatMessage.getBody(), null,
                new Channel(chatMessage.getChannelId()), new User(chatMessage.getSenderId()), createdTS);

        messageRespository.save(newMsg);

        String senderName = userRepository.findById(chatMessage.getSenderId()).get().getName();

        chatMessage.setCreatedTS(createdTS);
        chatMessage.setSenderName(senderName);
        msgTemplate.convertAndSend("/chatroom/" + chatMessage.getChannelId(), chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", "temSender");
        return chatMessage;
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