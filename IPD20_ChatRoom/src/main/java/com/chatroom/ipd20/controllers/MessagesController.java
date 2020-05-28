package com.chatroom.ipd20.controllers;

import com.chatroom.ipd20.entities.Message;
import com.chatroom.ipd20.services.SQLRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/
@RestController
public class MessagesController {

    @Autowired
    SQLRespository repo;

    @GetMapping("/send/{msg}")
    public Message sendMessage(@PathVariable String msg){

        Message newMsg = new Message(0,1, msg);
        repo.save(newMsg);
        return newMsg;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages(){
        return (List<Message>) repo.findAll();
    }
}
