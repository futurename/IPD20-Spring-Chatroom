package com.chatroom.ipd20.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/27
 **/

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private int id;
}
