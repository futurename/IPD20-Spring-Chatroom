package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.ActiveChatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ActiveChattingRepository extends JpaRepository<ActiveChatting, Integer> {
}
