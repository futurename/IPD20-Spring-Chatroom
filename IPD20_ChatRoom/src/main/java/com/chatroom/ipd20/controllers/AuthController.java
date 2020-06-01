package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.User;
import com.chatroom.ipd20.models.UserForm;
import com.chatroom.ipd20.security.CustomUserDetails;
import com.chatroom.ipd20.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepo;

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        if(authentication != null) {
            Object object = authentication.getPrincipal();
            if(object instanceof CustomUserDetails){
                CustomUserDetails user = (CustomUserDetails) object;
                model.addAttribute("user", user);
            }
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

    @PostMapping("/register")
    public String registerUser(@Valid UserForm userForm, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("bindingResult",bindingResult);
            return "register";
        }

        userRepo.save(new User(userForm));
        return "redirect:/login";
    }
}
