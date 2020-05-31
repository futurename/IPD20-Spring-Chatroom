package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepo;

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if(principal != null) {
            model.addAttribute("user", principal.getName());
        }
    }


    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model, Principal principal, HttpSession session){
        if(principal != null){
            return "redirect:/";
        }
        model.addAttribute("error", error != null);
        return "login";
    }


    @GetMapping("/register")
    public String register(){
        return "register";
    }

}
