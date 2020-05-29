package com.chatroom.ipd20.models;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/29
 **/

@Setter
@Getter
public class ChatMessage {
    private MessageType type;
    private String body;
    private String sender;



    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}