package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.ChannelRepository;
import com.chatroom.ipd20.services.HibernateSearchService;
import com.chatroom.ipd20.services.MessageRepository;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
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
    private HibernateSearchService searchService;

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication, HttpSession session) {
        if(authentication != null) {
            Object object = authentication.getPrincipal();
            if(object instanceof CustomUserDetails){
                CustomUserDetails user = (CustomUserDetails) object;
                model.addAttribute("user", user);
            }
        }
    }

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
}
