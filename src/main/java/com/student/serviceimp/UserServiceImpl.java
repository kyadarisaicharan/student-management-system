package com.student.serviceimp;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.student.model.Users;
import com.student.respositity.UsersRepository;

@Service
public class UserServiceImpl implements UserDetailsService{
	
	private UsersRepository userRepository;
	
	public UserServiceImpl(UsersRepository  userRepository) {
		this.userRepository=userRepository;
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users=userRepository.findByUsername(username)
		.orElseThrow(()-> new UsernameNotFoundException("Invalid username"));
		
		
		return User.withUsername(username)
				.password(users.getPassword())
				.disabled(!users.isActive())
				.build();
				
	}
}