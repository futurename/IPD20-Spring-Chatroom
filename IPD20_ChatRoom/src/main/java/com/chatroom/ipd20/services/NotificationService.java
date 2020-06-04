package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.Channel;
import com.chatroom.ipd20.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NotificationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    SimpMessagingTemplate msgTemplate;

    @Async
    public void deleteChannel(Channel channel){
        Set<User> users = channel.getUsers();
        for(User user : users){
            Set<Channel> favChannels = user.getFavoriteChannels();
            favChannels.remove(channel);
            userRepository.save(user);
            msgTemplate.convertAndSend(
                    "/notification/" + user.getId(),
                    "Channel is deleted. Get out from here!"
            );
        }

        channelRepository.delete(channel);
    }
}
