package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.ActiveChatting;
import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.ChatMessage;
import com.chatroom.ipd20.models.ConnChannel;
import com.chatroom.ipd20.models.FavChannel;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.ActiveChattingRepository;
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
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    ActiveChattingRepository activeChattingRepository;

    @Autowired
    public ChatController(SimpMessagingTemplate template) {
        this.msgTemplate = template;
    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage chatMessage, Authentication authentication) {
        LocalDateTime createdTS = LocalDateTime.now();
        String messsage = processMsg(chatMessage.getBody());

        Message newMsg = new Message(0, messsage, null,
                new Channel(chatMessage.getChannelId()), new User(chatMessage.getSenderId()), createdTS);

        messageRepository.save(newMsg);


        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findById(customUserDetails.getId()).get();

        String senderName = user.getName();

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
    public String getAllMessages(Model model, HttpServletResponse response, Authentication authentication) {
        model.addAttribute("allChannels", channelRepository.findAll());

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getId();

        User user = userRepository.findById(userId).orElse(null);
        Set<Channel> favChannels = user.getFavoriteChannels();
        model.addAttribute("allFavChannels", favChannels);
        model.addAttribute("userId", userId);
        model.addAttribute("activeChannels", activeChattingRepository.findAllByUserId(userId));

        return "index";
    }

    @PostMapping("/chatroom/requestSessionId")
    @ResponseBody
    public String checkSessionId(String sessionId) {
//        return "true";
        if (sessionId.contains("undefined")) {
            String springSessionPrefix = RequestContextHolder.currentRequestAttributes().getSessionId();
            sessionId = generateSessionId(springSessionPrefix);
        }
        return sessionId;
    }

    private String generateSessionId(String sessionId) {
        final int NUM_RANGE = 1000000;
        Random random = new Random();
        int randNum = random.nextInt(NUM_RANGE);
        return sessionId + "_append_" + randNum;
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

        msgTemplate.convertAndSend("/status/" + favChannel.getUserId(), "refresh");
    }

    @DeleteMapping("/chatroom/delFavChannel")
    @ResponseBody
    public void deleteFavChannel(@RequestBody FavChannel favChannel) {
        User user = userRepository.findById(favChannel.getUserId()).orElse(null);
        Set<Channel> favChannels = user.getFavoriteChannels();
        favChannels.removeIf(a -> a.getId() == favChannel.getChannelId());
        userRepository.save(user);
    }

    @GetMapping(value = {"/chatroom/leave", "/chatroom/leave/{channelId}"})
    public String leaveChatRoomInvalid(@PathVariable int channelId, Model model, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        int userId = userDetails.getId();
        User curUser = userRepository.findById(userId).orElse(null);
        model.addAttribute("activeChannelStatus", true);
        model.addAttribute("errorMsg", "You are not allowed or have joined this chatroom!");
        model.addAttribute("allChannels", channelRepository.findAll());
        Set<Channel> favChannels = curUser.getFavoriteChannels();
        model.addAttribute("allFavChannels", favChannels);
        model.addAttribute("userId", userId);
        model.addAttribute("activeChannels", activeChattingRepository.findAllByUserId(userId));
        return "/index";
    }

    @GetMapping("/chatroom/leave/{channelId}/{uniqueId}")
    public String leaveChatRoom(@PathVariable int channelId, Model model,
                                @PathVariable String uniqueId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        int userId = userDetails.getId();
        List<ActiveChatting> allActiveChattings = activeChattingRepository.findAll();
        boolean isCurChattingExist = allActiveChattings.stream().anyMatch(a -> a.getUniqueId().equals(uniqueId) &&
                a.getUserId() == userId && a.getChannelId() == channelId);

        if (isCurChattingExist) {
            ActiveChatting curActiveChatting = activeChattingRepository.findByUserIdAndChannelIdAndUniqueId(userId, channelId,
                    uniqueId).get(0);

            activeChattingRepository.delete(curActiveChatting);
        }
        User user = userRepository.findById(userId).get();

        model.addAttribute("allChannels", channelRepository.findAll());
        Set<Channel> favChannels = user.getFavoriteChannels();
        model.addAttribute("allFavChannels", favChannels);
        model.addAttribute("userId", userId);
        model.addAttribute("activeChannels", activeChattingRepository.findAllByUserId(userId));

        Channel channel = channelRepository.findById(channelId).orElse(null);

        if(channel != null) {

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
        }
        msgTemplate.convertAndSend("/status/" + userId, "refresh");

        return "index";
    }

    @PostMapping("/chatroom/validateStatus")
    @ResponseBody
    public int validateChattingStatus(String uniqueId){
        List<ActiveChatting> activeChattingList = activeChattingRepository.findAllByUniqueId(uniqueId);

        int count = activeChattingList.size();

        activeChattingList.forEach(a->activeChattingRepository.delete(a));

        return count;
    }
}