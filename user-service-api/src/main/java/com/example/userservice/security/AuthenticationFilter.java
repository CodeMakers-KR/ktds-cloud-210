package com.example.userservice.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.userservice.utils.Sha;
import com.example.userservice.vo.AuthenticateUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
	private String secretKey;
	
	public AuthenticationFilter(
			UserDetailsService userDetailsService,
			AuthenticationManager authenticationManager,
			String secretKey) {
		this.secretKey = secretKey;
		
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
			
			UserDetails userData = this.userService.loadUserByUsername(email);
			
			// password 암호화
			String salt = ((AuthenticateUser) userData).getSalt();
			password = Sha.getEncrypt(password, salt);
			
			// Config 후 재설정.
			return super.getAuthenticationManager().authenticate( 
								new UsernamePasswordAuthenticationToken(
									userData.getUsername(),
									password,
									userData.getAuthorities()) );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return super.attemptAuthentication(request, response);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		AuthenticateUser userDetail = (AuthenticateUser) authResult.getPrincipal();
		Date now = new Date();

		// 1. 토큰의 유효기간 설정.
		Date expiredDate = new Date(now.getTime() + Duration.ofDays(30).toMillis());

		// 2. 토큰의 암호화를 위한 비밀키 생성.
		SecretKey secretKey = Keys.hmacShaKeyFor(this.secretKey.getBytes());

		// 3. JsonWebToken 생성 및 반환.
		String token = Jwts.builder().subject(userDetail.getUserData().getUserId()).issuedAt(now) // 토큰을 발행한 날짜와
																										// 시간
				.expiration(expiredDate) // 토큰이 만료되는 날짜와 시간
				.signWith(secretKey) // 암호화에 사용될 키.
				.compact();
		
		response.addHeader("token", token);
		response.addHeader("email", userDetail.getUsername());
	}
	
}








