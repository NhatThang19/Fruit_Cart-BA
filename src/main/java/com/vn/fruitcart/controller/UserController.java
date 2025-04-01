package com.vn.fruitcart.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.turkraft.springfilter.boot.Filter;
import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.request.UserReqDTO;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.domain.dto.response.UserResDTO;
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
	public ResponseEntity<UserResDTO> createUser(@Valid @RequestBody User userReq) {
		User createUser = this.userServiceImpl.hadnleCreateUser(userReq);
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResUser(createUser));
	}

	@PutMapping()
	@ApiMessage("Update user")
	public ResponseEntity<UserResDTO> updateUser(@Valid @RequestBody UserReqDTO userReq) {
		User updateUser = this.userServiceImpl.handleUpdateUser(userReq);
		return ResponseEntity.ok(UserMapper.toResUser(updateUser));
	}

	@DeleteMapping("/{id}")
	@ApiMessage("Delete user")
	public ResponseEntity<Void> deleteUser(@PathVariable long id) {
		this.userServiceImpl.handleDeleteUser(id);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}")
	@ApiMessage("Get user by user")
	public ResponseEntity<UserResDTO> getUserById(@PathVariable long id) {
		User user = this.userServiceImpl.handleGetUserById(id);
		return ResponseEntity.ok(UserMapper.toResUser(user));
	}

	@GetMapping()
	@ApiMessage("Get all users")
	public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(this.userServiceImpl.handleGetAllUsers(spec, pageable));
	}

}
