package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.Message;
import org.springframework.data.repository.CrudRepository;



public interface MessageRespository extends CrudRepository<Message, Integer> {

}
