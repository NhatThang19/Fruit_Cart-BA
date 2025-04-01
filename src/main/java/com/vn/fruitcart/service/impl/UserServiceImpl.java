package com.vn.fruitcart.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.request.UserReqDTO;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.domain.dto.response.UserResDTO;
import com.vn.fruitcart.exception.ResourceNotFoundException;
import com.vn.fruitcart.repository.UserRepository;
import com.vn.fruitcart.service.UserService;
import com.vn.fruitcart.util.constans.GenderEnum;
import com.vn.fruitcart.util.mapper.UserMapper;

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
	public User handleGetUserById(long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return user;
	}

	@Override
	public ResultPaginationDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {
		Page<User> pageUser = this.userRepository.findAll(spec ,pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageUser.getTotalPages());
		mt.setTotal(pageUser.getTotalElements());

		rs.setMeta(mt);

		List<UserResDTO> listUser = pageUser.getContent().stream().map(UserMapper::toResUser)
				.collect(Collectors.toList());

		rs.setResult(listUser);

		return rs;
	}

	@Override
	public List<User> handleGetAllUsers() {
		return this.userRepository.findAll();
	}

	@Override
	public User handleUpdateUser(UserReqDTO userUpdate) {
		User existingUser = this.handleGetUserById(userUpdate.getId());

		existingUser.setUsername(userUpdate.getUsername());
		existingUser.setGender(GenderEnum.valueOf(userUpdate.getGender()));
		existingUser.setPhone(userUpdate.getPhone());
		existingUser.setAddress(userUpdate.getAddress());
		existingUser.setAvatar(userUpdate.getAvatar());
		existingUser.setEnabled(userUpdate.isEnabled());
		
		return this.userRepository.save(existingUser);
	}

	@Override
	public void handleDeleteUser(long id) {
		User existingUser = this.handleGetUserById(id);

		userRepository.delete(existingUser);
	}

}
