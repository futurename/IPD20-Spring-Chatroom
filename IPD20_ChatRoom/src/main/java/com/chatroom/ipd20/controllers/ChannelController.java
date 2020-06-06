package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.ActiveChatting;
import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.ActiveChattingForm;
import com.chatroom.ipd20.models.ChannelForm;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ChannelController {

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HibernateSearchService searchService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    BlobService blobService;
    @Autowired
    SimpMessagingTemplate msgTemplate;
    @Autowired
    ActiveChattingRepository activeChattingRepository;

    @GetMapping("/ajax/chatroom")
    public String allChatroom(String keyword, String page, Model model){
        int pageNum = 1;

        try { pageNum = Integer.parseInt(page); }
        catch (NumberFormatException ex){
            // Do nothing;
        }

        int totalPage = searchService.getTotalChannelPage(keyword);
        List<Channel> allChannels;

        if(totalPage != 0) {
            if(pageNum < 1){ pageNum = 1; }
            else if(pageNum > totalPage){ pageNum = totalPage; }

            allChannels = searchService.channelSearch(keyword, pageNum, totalPage);
        } else {
            allChannels = new ArrayList<Channel>();
        }
        model.addAttribute("allChannels", allChannels);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", pageNum);

        return "chatroomList";
    }

    @GetMapping("/chatroom/{channelId}")
    public String enterChatroomGetRedirect(Model model, @PathVariable int channelId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        int userId = userDetails.getId();
        User curUser = userRepository.findById(userId).orElse(null);
       // model.addAttribute("activeChannelStatus", true);
       // model.addAttribute("errorMsg", "You are not allowed or have joined this chatroom!");
        model.addAttribute("allChannels", channelRepository.findAll());
        Set<Channel> favChannels = curUser.getFavoriteChannels();
        model.addAttribute("allFavChannels", favChannels);
        model.addAttribute("userId", userId);
        model.addAttribute("activeChannels", activeChattingRepository.findAllByUserId(userId));
        return "index";
    }

    @GetMapping("/chatroom/{channelId}/{uniqueId}")
    public String enterChatroomGet(Model model, @PathVariable int channelId, @PathVariable String uniqueId, Authentication auth) {

        //Check whether this channelId exists in ActiveChannels table.
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        int userId = userDetails.getId();
        User curUser = userRepository.findById(userId).orElse(null);

        List<ActiveChatting> allActiveChattings = activeChattingRepository.findAll();
        boolean isExistsInActiveChannelTable = allActiveChattings.stream().anyMatch(a ->
                a.getChannelId() == channelId &&
                        a.getUserId() == userId);

        if (isExistsInActiveChannelTable) {
            model.addAttribute("activeChannelStatus", true);
            model.addAttribute("errorMsg", "You are not allowed or have joined this chatroom!");
            model.addAttribute("allChannels", channelRepository.findAll());
            Set<Channel> favChannels = curUser.getFavoriteChannels();
            model.addAttribute("allFavChannels", favChannels);
            model.addAttribute("userId", userId);
            model.addAttribute("activeChannels", activeChattingRepository.findAllByUserId(userId));
            return "index";
        } else {
            ActiveChatting newActiveChatting = new ActiveChatting(0, userId, channelId, uniqueId);
            activeChattingRepository.save(newActiveChatting);

            Channel curChannel = channelRepository.findById(channelId).get();
            List<User> userList = userRepository.findAll();
            List<Message> messageList = messageRepository.findByChannel(new Channel(channelId));

            model.addAttribute("curChannel", curChannel);
            model.addAttribute("userList", userList);
            model.addAttribute("messageList", messageList);
            model.addAttribute("userId", userId);

            Set<Channel> allActiveChannels = curUser.getActiveChannels();
            Channel activeChannel = new Channel();
            activeChannel.setId(channelId);
            activeChannel.setOwner(new User(userId));
            allActiveChannels.add(activeChannel);
            userRepository.save(curUser);

            return "chatroom";
        }
    }

    @GetMapping("/chatroom")
    public String index() {
        return "chatroom_form";
    }

    @PostMapping("/chatroom")
    public String create(
            @Valid ChannelForm channelForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "chatroom_form";
        }

        // Set Icon image. Scale down and convert to png extension.
        if (!channelForm.getIcon().isEmpty()) {
            try {
                Blob iconBlob = blobService.createBlob(channelForm.getIcon().getInputStream());
                channelForm.setIconBlob(iconBlob);
            } catch (IOException ex) {
                System.out.println("Fail to scale down icon");
            }
        }

        User owner = new User(((CustomUserDetails) authentication.getPrincipal()).getId());
        channelRepository.save(new Channel(channelForm, owner));
        return "redirect:/";
    }

    @DeleteMapping("/chatroom/{id}")
    @ResponseBody
    public String delete(@PathVariable int id, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Channel channel = channelRepository.findById(id).orElse(null);

        if (channel == null) {
            return "false";
        }

        User owner = channel.getOwner();
        if (owner.getId() != userDetails.getId()) {
            return "false";
        }

        msgTemplate.convertAndSend("/chatroomNotification/" + channel.getId(), "end");
        notificationService.deleteChannel(channel);

        return "true";
    }
}
