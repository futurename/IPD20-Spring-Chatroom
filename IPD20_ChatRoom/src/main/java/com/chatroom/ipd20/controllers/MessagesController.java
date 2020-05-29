package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.services.SQLRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/
@Controller
public class MessagesController {

    @Autowired
    SQLRespository repo;

    @GetMapping("/send/{msg}")
    @ResponseBody
    public Message sendMessage(@PathVariable String msg){

        Message newMsg = new Message(1, msg);
        repo.save(newMsg);
        return newMsg;
    }

    @GetMapping("/messages")
    public String  getAllMessages(Model model){
        model.addAttribute("msgList",repo.findAll());
        return "index";
    }

}
