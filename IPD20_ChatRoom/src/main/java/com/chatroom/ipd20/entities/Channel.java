package com.chatroom.ipd20.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/

@Entity
@Data
@Table(name="channels")
public class Channel {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @NotEmpty
    @Size(min=1, max=100)
    @Column(length=100)
    private String title;

    @NotEmpty
    @Size(min=1, max=200)
    @Column(length=200)
    private String description;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name="ownerId")
    private User owner;

    @NotEmpty
    @OneToMany(mappedBy = "channel")
    private Set<Message> messages;

    @ManyToMany(mappedBy = "favoriteChannels")
    private Set<User> users;

    @NotEmpty
    private LocalDateTime createdTS;
}
