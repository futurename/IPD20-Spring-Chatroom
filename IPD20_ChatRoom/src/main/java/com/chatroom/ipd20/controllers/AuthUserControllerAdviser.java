package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.BlobService;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Blob;

@ControllerAdvice
public class AuthUserControllerAdviser {
    @Autowired
    BlobService blobService;

    @Autowired
    UserRepository userRepository;

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        if(authentication == null) { return; }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId()).orElseThrow(() ->
                new IllegalArgumentException("Internal Error. Cannot find user id: " + userDetails.getId())
        );

        Blob icon = user.getIcon();
        if(icon != null){
            String base64Icon = blobService.blobToBase64String(icon);
            model.addAttribute("base64Icon", base64Icon);
        }
        model.addAttribute("totalMsg", user.getMessages().size());
        model.addAttribute("totalOwnChannel", user.getChannels().size());
        model.addAttribute("totalFavChannel", user.getFavoriteChannels().size());
        model.addAttribute("user", user);
    }

}
