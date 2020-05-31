package com.chatroom.ipd20.services;

import com.chatroom.ipd20.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Wei Wang
 * @version 1.0
 * @since 2020/05/30
 **/

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
