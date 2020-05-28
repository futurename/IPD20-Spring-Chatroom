package com.chatroom.ipd20.entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.naming.Name;
import javax.persistence.*;
import java.sql.Date;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/



@Entity
@Table(name="messages")
@NoArgsConstructor

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private int id;

    @Setter
    private int userId;

    @Getter
    @Setter
    private String body;

    public Message(int id, int userId, String body) {
        this.id = id;
       this.userId = userId;
       this.body = body;
    }

    @Column(name = "userId")
    public int getUserId() {
        return userId;
    }
}
