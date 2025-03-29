package com.vn.fruitcart.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.repository.UserRepository;
import com.vn.fruitcart.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User hadnleCreateUser(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		return this.userRepository.save(user);
	}

	@Override
	public Optional<User> handleGetUserById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Page<User> handleGetAllUsers(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> handleGetAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User handleUpdateUser(Long id, User userDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleDeleteUser(Long id) {
		// TODO Auto-generated method stub
		
	}

}
