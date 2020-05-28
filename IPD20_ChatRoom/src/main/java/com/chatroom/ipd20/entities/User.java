package com.chatroom.ipd20.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Size(min=5, max=50)
    private String email;

    @NotEmpty
    @Size(min=1, max=50)
    private String name;

    @NotEmpty
    @Size(min=5, max=50)
    private String password;
}
