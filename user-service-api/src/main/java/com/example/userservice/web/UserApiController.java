package com.example.userservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.userservice.exceptions.UserException;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RegistUserVO;
import com.example.userservice.vo.ResponseOrderVO;
import com.example.userservice.vo.ResponseUserVO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user-service-api")
public class UserApiController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserService userService;

	@PostMapping("/users")
	public ResponseEntity<ResponseUserVO> createUser(@RequestBody @Valid RegistUserVO registUserVO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new UserException(bindingResult.getFieldErrors());
		}
		ResponseUserVO responseUser = userService.createNewUser(registUserVO);
		return new ResponseEntity<ResponseUserVO>(responseUser, HttpStatusCode.valueOf(201));
	}

	@GetMapping("/users")
	public ResponseEntity<List<ResponseUserVO>> getAllUsers() {
		List<ResponseUserVO> responseUser = userService.fetchAllUsers();
		return new ResponseEntity<List<ResponseUserVO>>(responseUser, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseUserVO> getOneUsers(@PathVariable String userId) {
		ResponseUserVO responseUser = userService.fetchOneUser(userId);
		
		// Order Service 특정 사용자의 주문 정보.
		// order service 호출을 위한 url
		String orderUrl = "http://ORDER-SERVICE-API/order-service-api/%s/orders";
		ResponseEntity<List<ResponseOrderVO>> usersOrder = 
				this.restTemplate.exchange(
					orderUrl.formatted(userId),
					HttpMethod.GET,
					null, // HttpHeader
					new ParameterizedTypeReference<>() {});
		
		responseUser.setOrders(usersOrder.getBody());
		
		return new ResponseEntity<ResponseUserVO>(responseUser, HttpStatusCode.valueOf(200));
	}

	
}



