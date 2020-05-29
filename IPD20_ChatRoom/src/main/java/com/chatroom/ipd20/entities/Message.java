package com.chatroom.ipd20.entities;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Size(min=1, max=1000)
    @Column(length=1000, nullable = false)
    private String body;

    @Column(length=200)
    private String filePath;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name="channelId", nullable = false)
    private Channel channel;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;

    @NotEmpty
    private LocalDateTime createdTS = LocalDateTime.now();

    public Message(int userId, String body) {
        this.user = new User(userId);
           this.body = body;
    }
}
