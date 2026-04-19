package com.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringConfig {
	
	private static final String[] PUBLIC_PATH= {
			"/login", "/css/**", "/images/**", "/js/**", "/error", "/enrollments/enroll", "/enrollments/enrollCourse" 
	};
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	                .requestMatchers(PUBLIC_PATH).permitAll()
	                .requestMatchers("/enrollments/**").authenticated() 
	                .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	                .loginPage("/login")
	                .loginProcessingUrl("/login")
	                .defaultSuccessUrl("/dashboard", true)
	                .permitAll()
	        )
	        .logout(logout -> logout
	        		.logoutUrl("/logout")
	                .logoutSuccessUrl("/login?logout")
	                .invalidateHttpSession(true)
	                .clearAuthentication(true)
	                .deleteCookies("JESSIONID")
	                .permitAll()
	        );

	    return http.build();
	}
	
	
	
	@Bean
	public PasswordEncoder passWordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
