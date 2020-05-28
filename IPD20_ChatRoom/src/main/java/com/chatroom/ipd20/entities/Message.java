package com.chatroom.ipd20.entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.naming.Name;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/



@Entity
@Data
@NoArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @NotEmpty
    @Size(min=1, max=1000)
    private String body;

    private String filePath;

    @NotEmpty
    private int userId;

    @NotEmpty
    private int channelId;

    @NotEmpty
    private LocalDateTime createdTS;


    public Message(int id, int userId, String body) {
        this.id = id;
       this.userId = userId;
       this.body = body;
    }


}
