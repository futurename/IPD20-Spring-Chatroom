package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.ActiveChatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.validation.constraints.NotNull;
import java.util.List;

@RepositoryRestResource
public interface ActiveChattingRepository extends JpaRepository<ActiveChatting, Integer> {
    List<ActiveChatting> findByUserIdAndChannelIdAndUniqueId(@NotNull int userId, @NotNull int channelId,
                                                               @NotNull String uniqueId);
    List<ActiveChatting> findAllByUserId(@NotNull int userId);

    List<ActiveChatting> findAllByChannelId(@NotNull int channelId);
}
