package com.chatroom.ipd20.entities;

import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name="channelId", nullable = false)
    @ToString.Exclude
    private Channel channel;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    @ToString.Exclude
    private User user;

    @NotNull
    private LocalDateTime createdTS = LocalDateTime.now();

    public Message(int userId, String body) {
        this.user = new User(userId);
           this.body = body;
    }
}
