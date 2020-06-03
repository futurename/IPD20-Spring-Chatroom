package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.UserForm;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.BlobService;
import com.chatroom.ipd20.services.ChannelRepository;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    BlobService blobService;

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            Model model,
            Principal principal,
            HttpSession session)
    {
        if(principal != null){
            return "redirect:/";
        }
        model.addAttribute("error", error != null);
        return "login";
    }


    @GetMapping("/register")
    public String register(Principal principal){
        if(principal != null){
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid UserForm userForm,
            BindingResult bindingResult, Model model)
    {
        if(bindingResult.hasErrors()){
            model.addAttribute("bindingResult",bindingResult);
            return "register";
        }

        // Set Icon image. Scale down and convert to png extension.
        if(!userForm.getIcon().isEmpty()) {
            try {
                Blob iconBlob = blobService.createBlob(userForm.getIcon().getInputStream());
                userForm.setIconBlob(iconBlob);
            } catch (IOException ex) {
                System.out.println("Fail to scale down icon");
            }
        }

        userRepository.save(new User(userForm));
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }
}
