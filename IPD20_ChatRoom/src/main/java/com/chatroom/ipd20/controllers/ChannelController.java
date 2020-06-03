package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.ChannelForm;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    BlobService blobService;

    @GetMapping("/channel")
    public String search(@RequestParam(required = false) String search, Model model) {
        List<Channel> searchResults = new ArrayList<>();

        if (search == null || search.isEmpty()) {
            model.addAttribute("searchList", searchResults);
            return "channel";
        }

        searchResults = searchService.channelSearch(search);
        model.addAttribute("searchList", searchResults);
        return "channel";
    }

    @GetMapping("/chatroom/{id}")
    public String enterChatroom(Model model, @PathVariable int id) {
        int channelId = id;
        Channel curChannel = channelRepository.findById(channelId).get();
        List<User> userList = userRepository.findAll();

        List<Message> messageList = messageRepository.findByChannel(new Channel(channelId));

        model.addAttribute("curChannel", curChannel);
        model.addAttribute("userList", userList);
        model.addAttribute("messageList", messageList);
        return "chatroom";
    }


    @GetMapping("/chatroom")
    public String index(){
        return "chatroom_form";
    }

    @PostMapping("/chatroom")
    public String create(
            @Valid ChannelForm channelForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model)
    {
        if(bindingResult.hasErrors()){
            model.addAttribute("bindingResult",bindingResult);
            return "chatroom_form";
        }

        // Set Icon image. Scale down and convert to png extension.
        if(!channelForm.getIcon().isEmpty()) {
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
}
