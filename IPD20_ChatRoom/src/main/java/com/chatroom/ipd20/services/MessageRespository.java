package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.temporal.ValueRange;
import java.util.List;


public interface MessageRespository extends JpaRepository<Message, Integer> {

/*    @Query("FROM Message m WHERE m.channel = :channelId")
    List<Message> findMessageByChannelId(@Param("channelId") int channelId);*/


    List<Message> findByChannelId(int idChannel);

}
