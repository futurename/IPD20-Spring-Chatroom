package com.chatroom.ipd20.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @GeneratedValue
    private int id;

}
