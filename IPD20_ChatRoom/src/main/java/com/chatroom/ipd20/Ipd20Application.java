package com.chatroom.ipd20;

import com.chatroom.ipd20.services.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class Ipd20Application {

	public static void main(String[] args) {
		SpringApplication.run(Ipd20Application.class, args);

		System.out.println("Running project!!!!!!!!!!!!!!");
	}

}
