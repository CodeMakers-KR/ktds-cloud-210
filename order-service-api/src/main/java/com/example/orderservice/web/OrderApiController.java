package com.example.orderservice.web;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-service-api")
public class OrderApiController {

	@PostMapping("/orders")
	public Map<String, Object> makeNewOrder() {
		return Map.of("result", "order-id");
	}
	
}
