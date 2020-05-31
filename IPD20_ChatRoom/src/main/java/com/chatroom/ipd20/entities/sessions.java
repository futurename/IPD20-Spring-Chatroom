package com.chatroom.ipd20.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class sessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
