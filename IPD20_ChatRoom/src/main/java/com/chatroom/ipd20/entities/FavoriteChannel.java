package com.chatroom.ipd20.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="favoriteChannels")
public class FavoriteChannel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @NotEmpty
    private int userId;

    @NotEmpty
    private int channelId;

    @NotEmpty
    private LocalDateTime createdTS;
}
