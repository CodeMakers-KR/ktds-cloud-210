package com.example.apigatewayservice.filters;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	@Value("${token.secret}")
	private String jwtSecret;

	public AuthorizationHeaderFilter() {
		super(Config.class); // Filter List에 등록
	}

	public static class Config {
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			// Pre Filter
			ServerHttpRequest request = exchange.getRequest();
			if (!request.getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "인증이 필요합니다.", HttpStatus.UNAUTHORIZED);
			}

			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authorizationHeader.replace("Bearer ", "");
			if (!isJwtValid(jwt)) {
				return onError(exchange, "만료되거나 변조된 토큰입니다.", HttpStatus.UNAUTHORIZED);
			}

			return chain.filter(exchange);
		};
	}

	private boolean isJwtValid(String jwt) {
		SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes());
		String subject = null;

		try {
			subject = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload().getSubject();
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}

		if (subject == null || subject.isEmpty()) {
			return false;
		}

		return true;
	}

	private Mono<Void> onError(ServerWebExchange exchange, String string, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		byte[] bytes = "JWT가 올바르지 않습니다.".getBytes();
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return response.writeWith(Flux.just(buffer));
	}
}