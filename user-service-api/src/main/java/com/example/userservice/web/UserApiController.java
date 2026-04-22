package com.example.userservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.service.UserService;
import com.example.userservice.vo.RegistUserVO;
import com.example.userservice.vo.ResponseUserVO;

@RestController
@RequestMapping("/user-service-api")
public class UserApiController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/users")
	public ResponseUserVO createUser( 
			@RequestBody RegistUserVO registUserVO ) {
		ResponseUserVO user = this.userService.createNewUser(registUserVO);
		return user;
	}
	
	@GetMapping("/users")
	public List<ResponseUserVO> getAllUsers() {
		return this.userService.fetchAllUsers();
	}
	
}
