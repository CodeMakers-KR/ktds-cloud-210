package com.example.userservice.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationHeaderFilter extends OncePerRequestFilter {

	private String jwtSecret;

	public AuthorizationHeaderFilter(String jwtSecret) {
	    this.jwtSecret = jwtSecret;
	  }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String ignoreAuthorization = request.getHeader("ignore-authorization");
		if ("ignore-auth".equals(ignoreAuthorization)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String jwt = request.getHeader("Authorization");

		
		if (jwt != null) {
			jwt = jwt.replace("Bearer ", "");
			String subject = getSubjectInJWT(jwt);

			if (subject != null) {
				SecurityContextHolder.getContext()
						.setAuthentication(getOneTimeAuthencation(subject, new ArrayList<>()));
			}
		} else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().append("{error: \"인증이 필요합니다.\"}").flush();
			return;
		}

		filterChain.doFilter(request, response);
	}

	private String getSubjectInJWT(String jwt) {
		SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes());

		String subject = null;
		try {
			subject = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload().getSubject();
		} catch (JwtException | IllegalArgumentException ex) {
			return null;
		}

		if (subject == null || subject.isEmpty()) {
			return null;
		}

		return subject;
	}

	private Authentication getOneTimeAuthencation(String email, Collection<? extends GrantedAuthority> authorities) {
		return new UsernamePasswordAuthenticationToken(email, null, authorities);
	}

}
