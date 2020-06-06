package com.chatroom.ipd20.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/06/05
 **/
@Indexed
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ActiveChattings")
public class ActiveChatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int userId;

    @NotNull
    private int channelId;

    @NotNull
    private String uniqueId;


}
