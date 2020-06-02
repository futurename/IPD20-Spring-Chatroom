package com.chatroom.ipd20.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/06/02
 **/

@Getter
@Setter
public class UserConnectInfo {
    private String senderName;
    private int channelId;
    private int senderId;
}
