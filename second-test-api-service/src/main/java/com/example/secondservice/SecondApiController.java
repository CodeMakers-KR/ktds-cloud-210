package com.example.secondservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/second-service")
public class SecondApiController {

	@Autowired
	private Environment env;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Hello, Spring Cloud - Second Api Service";
	}
	
	@GetMapping("/message")
	public String message(@RequestHeader("second-request") String userRequest) {
		return "Hello, Spring Cloud - Second Api service " + userRequest;
	}
	
	@GetMapping("/check")
	public String checkPort(HttpServletRequest request) {
		int requestPort = request.getServerPort();
		String serverPort = this.env.getProperty("local.server.port");
		return "request port: %d, server port: %s".formatted(requestPort, serverPort);
	}
}
