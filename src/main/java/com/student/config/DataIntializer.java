package com.student.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.student.model.Users;
import com.student.respositity.UsersRepository;

@Configuration
public class DataIntializer {
	@Bean
	CommandLineRunner loadSampleData(UsersRepository usersRepository,PasswordEncoder passWordEncoder) {
		return args ->{
			if(!usersRepository.existsByUsername("Admin")) {
				Users users= new Users();
				users.setUsername("Admin");
				users.setPassword(passWordEncoder.encode("admin@123"));
				users.setActive(true);
				usersRepository.save(users);
				
			}
		};
	}
	

}
