package com.vn.fruitcart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.service.impl.UserServiceImpl;
import com.vn.fruitcart.util.annotation.ApiMessage;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/api/users")
public class UserController {
	
	private final UserServiceImpl userServiceImpl;
	
	public UserController(UserServiceImpl userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}
	
	@PostMapping()
	@ApiMessage("Create new user")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User createUser = this.userServiceImpl.hadnleCreateUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
	}

}
