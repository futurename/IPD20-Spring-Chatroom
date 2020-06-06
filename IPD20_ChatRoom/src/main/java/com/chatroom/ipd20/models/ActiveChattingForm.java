package com.chatroom.ipd20.models;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/06/05
 **/
@Getter
@Setter
public class ActiveChattingForm {
    private int userId;
    private int channelId;
    private String uniqueId;
}
