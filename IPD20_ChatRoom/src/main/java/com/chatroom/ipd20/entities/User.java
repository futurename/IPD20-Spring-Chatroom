package com.chatroom.ipd20.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    public User(int id){
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Size(min=5, max=50)
    @Column(length=50)
    @Field
    private String email;

    @NotEmpty
    @Size(min=1, max=50)
    @Column(length=50)
    @ContainedIn
    @Field
    private String name;

    @NotEmpty
    @Size(min=5, max=50)
    @Column(length=50)
    private String password;


    @OneToMany(mappedBy="owner")
    @ToString.Exclude
    private Set<Channel> channels = new HashSet<Channel>();

    @OneToMany(mappedBy="user")
    @ToString.Exclude
    private Set<Message> messages = new HashSet<Message>();

    @ManyToMany
    @JoinTable(
            name = "favoriteChannels",
            joinColumns = { @JoinColumn(name = "userId") },
            inverseJoinColumns = { @JoinColumn(name = "channelId") }
    )
    @ToString.Exclude
    private Set<Channel> favoriteChannels;
}
