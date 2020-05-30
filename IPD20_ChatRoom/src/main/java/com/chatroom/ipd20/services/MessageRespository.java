package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MessageRespository extends JpaRepository<Message, Integer> {

/*    @Query("FROM Message m WHERE m.channel = :channelId")
    List<Message> findMessageByChannelId(@Param("channelId") int channelId);*/

   // List<Message> findByChannelId(int channelId);


    List<Message> findByChannel(Channel channel);
}
