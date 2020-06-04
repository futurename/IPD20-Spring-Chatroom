package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.ChatMessage;
import com.chatroom.ipd20.models.ConnChannel;
import com.chatroom.ipd20.models.FavChannel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/29
 **/

@Controller
public class ChatController {

    private final SimpMessagingTemplate msgTemplate;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    public ChatController(SimpMessagingTemplate template) {
        this.msgTemplate = template;
    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage chatMessage) {
        LocalDateTime createdTS = LocalDateTime.now();
        String messsage = processMsg(chatMessage.getBody());

        Message newMsg = new Message(0, messsage, null,
                new Channel(chatMessage.getChannelId()), new User(chatMessage.getSenderId()), createdTS);

        messageRepository.save(newMsg);

        String senderName = userRepository.findById(chatMessage.getSenderId()).get().getName();

        chatMessage.setCreatedTS(createdTS);
        chatMessage.setSenderName(senderName);
        chatMessage.setBody(messsage);

        msgTemplate.convertAndSend("/chatroom/" + chatMessage.getChannelId(), chatMessage);
    }

    private String processMsg(String body) {
        List<String> tokens = getAllTokens(body);
        String result = "";

        List<User> allUsers = userRepository.findAll();
        List<String> validTokens =
                tokens.stream().filter(a -> allUsers.stream().anyMatch(b -> b.getName().equals(a))).collect(Collectors.toList());

        for (String str : validTokens) {
            body = body.replaceAll("@" + str, "<span class='font-bold text-blue-500'>@</span>" +
                    "<span class='underline font-bold text-blue-500'>" + str + "</span>");
        }
        return body;
    }

    private List<String> getAllTokens(String body) {
        ArrayList<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("@[a-zA-Z]+").matcher(body);
        while (matcher.find()) {
            result.add(matcher.group().replace("@", ""));
        }
        return result;

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

        Message newMsg = new Message(0, connString, null,
                new Channel(connChannel.getChannelId()), new User(userId), createdTS);

        messageRepository.save(newMsg);

        ChatMessage connChatMsg = new ChatMessage();
        connChatMsg.setSenderName(senderName);
        connChatMsg.setType(ChatMessage.MessageType.JOIN);
        connChatMsg.setBody(connString);
        connChatMsg.setSenderId(userId);
        connChatMsg.setCreatedTS(createdTS);
        connChatMsg.setChannelId(connChannel.getChannelId());
        msgTemplate.convertAndSend("/chatroom/" + connChatMsg.getChannelId(), connChatMsg);

        msgTemplate.convertAndSend("/status/" + userId, "refresh");

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
        model.addAttribute("userId", userId);
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

        msgTemplate.convertAndSend("/status/" + favChannel.getUserId() , "refresh");
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
        model.addAttribute("userId", userId);
        model.addAttribute("activeChannels", user.getActiveChannels());


        String leaveString = "<span class='font-bold text-red-500 text-base'>" + user.getName() + "</span> just " +
                "left the chatroom...\uD83D\uDE02";

        LocalDateTime createdTS = LocalDateTime.now();
        Message newMsg = new Message(0, leaveString, null,
                new Channel(channelId), new User(userId), createdTS);

        messageRepository.save(newMsg);

        ChatMessage chatMessage = new ChatMessage();

        String senderName = userRepository.findById(userId).get().getName();

        chatMessage.setCreatedTS(createdTS);
        chatMessage.setSenderName(senderName);
        chatMessage.setBody(leaveString);

        msgTemplate.convertAndSend("/chatroom/" + channelId, chatMessage);
        msgTemplate.convertAndSend("/status/" + userId, "refresh");

        return "index";
    }


}