package com.vn.fruitcart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.response.ResUserDTO;
import com.vn.fruitcart.service.impl.UserServiceImpl;
import com.vn.fruitcart.util.annotation.ApiMessage;
import com.vn.fruitcart.util.mapper.UserMapper;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/users")
public class UserController {

	private final UserServiceImpl userServiceImpl;

	public UserController(UserServiceImpl userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	@PostMapping()
	@ApiMessage("Create new user")
	public ResponseEntity<ResUserDTO> createUser(@Valid @RequestBody User reqUser) {
		User createUser = this.userServiceImpl.hadnleCreateUser(reqUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResUser(createUser));
	}

}
