package com.example.userservice.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * API로 로그인 요청 -> body의 데이터 취득 (email, password)
 * UsernamePasswordAuthenticationFilter -> email로 회원 정보 조회 
 * -> password를 암호화 ->
 * 데이터베이스의 비밀번호와 비교.
 * 
 * -> 비교 결과 정상: 인증 객체 생성. UsernamePasswordAuthenticationToken
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UserDetailsService userService;
	private PasswordEncoder passwordEncoder;
	
	public AuthenticationFilter(
			UserDetailsService userDetailsService,
			AuthenticationManager authenticationManager) {
		this.userService = userDetailsService;
		super.setAuthenticationManager(authenticationManager);
	}
	
	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		// login 요청 (JSON) { email: "", password: "" }
		try {
			Map<String, String> credential = new ObjectMapper()
						.readValue(request.getInputStream(), HashMap.class);
			
			String email = credential.get("email");
			String password = credential.get("password");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return super.attemptAuthentication(request, response);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
}
