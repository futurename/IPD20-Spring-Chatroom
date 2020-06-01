package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByChannel(Channel channel);
}
