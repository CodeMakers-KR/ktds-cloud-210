package com.example.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user-service")
public class UserApiController {

	@Autowired
	private Environment env;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Hello, Spring Cloud - User Service";
	}
	
	@GetMapping("/message")
	public String message(@RequestHeader("user-request") String userRequest) {
		return "Hello, Spring Cloud - User service " + userRequest;
	}
	
	@GetMapping("/check")
	public String checkPort(HttpServletRequest request) {
		int requestPort = request.getServerPort();
		String serverPort = this.env.getProperty("local.server.port");
		return "request port: %d, server port: %s".formatted(requestPort, serverPort);
	}
}







