package com.example.userservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import com.example.userservice.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${token.secret}")
	private String secretKey;

	@Autowired
	private UserService userService;
	private PasswordEncoder passwordEncoder;

	public SecurityConfig() {
		this.passwordEncoder = new PasswordEncoderImpl();
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		AuthenticationManager authenticationManager = this.getAuthenticationManager(http);

		http.csrf(csrf -> csrf.disable())
				.authenticationManager(authenticationManager)
				.addFilter(getAuthenticationFilter(authenticationManager))
				.addFilterAfter(new AuthorizationHeaderFilter(secretKey),
						AnonymousAuthenticationFilter.class)
				.httpBasic(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> 
					auth.requestMatchers("/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/user-service/users").permitAll()
						.anyRequest().authenticated());

		return http.build();
	}

	private AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(this.userService).passwordEncoder(this.passwordEncoder);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		return authenticationManager;
	}

	private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
		return new AuthenticationFilter(this.userService, authenticationManager, this.secretKey);
	}

}
