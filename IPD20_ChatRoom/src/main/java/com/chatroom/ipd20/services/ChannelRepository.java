package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ChannelRepository extends JpaRepository<Channel, Integer> {

}
