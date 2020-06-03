package com.chatroom.ipd20.entities;

import com.chatroom.ipd20.models.ChannelForm;
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
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Indexed @Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="channels")
public class Channel {

    public Channel(int id){
        this.id = id;
    }
    public Channel(ChannelForm channelForm, User user){
        this.title = channelForm.getTitle();
        this.description = channelForm.getDescription();
        this.icon = channelForm.getIconBlob();
        this.owner = user;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min=5, max=100)
    @Column(length=100, nullable = false)
    @Field
    private String title;

    @NotNull
    @Size(min=5, max=200)
    @Column(length=200, nullable = false)
    @Field
    private String description;

    @Lob
    private Blob icon;

    @ManyToOne
    @JoinColumn(name="ownerId", nullable = false)
    @IndexedEmbedded
    @ToString.Exclude
    private User owner;

    @OneToMany(mappedBy = "channel")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Set<Message> messages = new HashSet<Message>();

    @ManyToMany(mappedBy = "favoriteChannels")
    @ToString.Exclude
    private Set<User> users = new HashSet<User>();

    @ManyToMany(mappedBy = "activeChannels")
    @ToString.Exclude
    private Set<User> activeUsers = new HashSet<User>();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdTS = LocalDateTime.now();
}
