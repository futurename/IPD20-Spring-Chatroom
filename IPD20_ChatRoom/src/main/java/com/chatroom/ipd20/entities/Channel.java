package com.chatroom.ipd20.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/

@Indexed
@Entity
@Data
@NoArgsConstructor
@Table(name="channels")
public class Channel {
    public Channel(int id){
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Size(min=1, max=100)
    @Column(length=100, nullable = false)
    @Field
    private String title;

    @NotEmpty
    @Size(min=1, max=200)
    @Column(length=200, nullable = false)
    @Field
    private String description;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name="ownerId", nullable = false)
    @IndexedEmbedded
    private User owner;

    @NotEmpty
    @OneToMany(mappedBy = "channel")
    private Set<Message> messages = new HashSet<Message>();

    @ManyToMany(mappedBy = "favoriteChannels")
    private Set<User> users = new HashSet<User>();

    @NotEmpty
    @Column(nullable = false)
    private LocalDateTime createdTS = LocalDateTime.now();
}
