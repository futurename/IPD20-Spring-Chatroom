package com.chatroom.ipd20.entities;

import com.chatroom.ipd20.models.UserForm;
import lombok.*;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    public User(int id){
        this.id = id;
    }
    public User(UserForm userForm){
        this.email = userForm.getEmail();
        this.name = userForm.getName();
        this.password = userForm.getPass1();
        this.icon = userForm.getIconBlob();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min=5, max=50)
    @Column(length=50, nullable = false, unique = true)
    @Field
    private String email;

    @NotEmpty
    @Size(min=1, max=50)
    @Column(length=50, nullable = false)
    @ContainedIn
    @Field
    private String name;

    @NotEmpty
    @Size(min=5, max=50)
    @Column(length=50, nullable = false)
    private String password;

    @Lob
    private Blob icon;

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
