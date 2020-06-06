package com.chatroom.ipd20.entities;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Indexed
@Entity @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min=1, max=1000)
    @Column(length=1000, nullable = false)
    @Field
    private String body;

    @Column(length=200)
    private String filePath;

    @ManyToOne
    @JoinColumn(name="channelId", nullable = false)
    @ToString.Exclude
    private Channel channel;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    @IndexedEmbedded
    @ToString.Exclude
    private User user;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdTS = LocalDateTime.now();

    public Message(int userId, String body) {
        this.user = new User(userId);
           this.body = body;
    }
}
