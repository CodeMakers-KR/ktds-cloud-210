package com.example.userservice.vo;

import java.util.ArrayList;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticateUser implements UserDetails {

	private static final long serialVersionUID = 1862652386448226440L;

	private ResponseUserVO userData;
	
	public AuthenticateUser(ResponseUserVO userData) {
		this.userData = userData;
	}
	
	public ResponseUserVO getUserData() {
		return userData;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public @Nullable String getPassword() {
		return this.userData.getPwd();
	}

	@Override
	public String getUsername() {
		return this.userData.getEmail();
	}
	
	public String getSalt() {
		return this.userData.getSalt();
	}

}
