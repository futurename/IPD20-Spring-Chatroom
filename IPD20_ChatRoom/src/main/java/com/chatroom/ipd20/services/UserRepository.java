package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/30
 **/

public interface UserRepository extends CrudRepository<User, Integer> {
}
