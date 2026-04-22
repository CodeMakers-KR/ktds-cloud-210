package com.example.firstservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/first-service")
public class FirstApiController {

	@Autowired
	private Environment env;
	
	@GetMapping("/welcome")
	public String welcome(
			@RequestHeader("f-request") String fRequest,
			@RequestHeader("f-secret") String secret,
			@RequestHeader("first-request") String firstRequest
			) {
		return "Hello, Spring Cloud - First Api Service %s %s %s"
				.formatted(fRequest, secret, firstRequest);
	}
	
	@GetMapping("/message")
	public String message(@RequestHeader("first-request") String userRequest) {
		return "Hello, Spring Cloud - First Api service " + userRequest;
	}
	
	@GetMapping("/check")
	public String checkPort(HttpServletRequest request) {
		int requestPort = request.getServerPort();
		String serverPort = this.env.getProperty("local.server.port");
		return "request port: %d, server port: %s".formatted(requestPort, serverPort);
	}
}
