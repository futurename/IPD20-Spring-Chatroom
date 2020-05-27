package com.chatroom.ipd20.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/

@RestController
public class TestFreeMarkerController {

    @GetMapping("/welcome/{name}")
    public String hello(Model model, @PathVariable String name){
        model.addAttribute("name", name);
        return name;
    }
}
