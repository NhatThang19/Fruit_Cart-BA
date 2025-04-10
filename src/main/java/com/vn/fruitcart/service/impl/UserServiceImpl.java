package com.vn.fruitcart.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.UserAuditLog;
import com.vn.fruitcart.domain.dto.request.UserReqDTO;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.domain.dto.response.UserResDTO;
import com.vn.fruitcart.exception.ResourceNotFoundException;
import com.vn.fruitcart.repository.UserAuditLogRepository;
import com.vn.fruitcart.repository.UserRepository;
import com.vn.fruitcart.service.UserService;
import com.vn.fruitcart.util.constant.ActionLogEnum;
import com.vn.fruitcart.util.constant.GenderEnum;
import com.vn.fruitcart.util.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserAuditLogRepository auditLogRepository;
	private final ObjectMapper objectMapper;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			UserAuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.auditLogRepository = auditLogRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	@Transactional
	public User handleCreateUser(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		this.userRepository.save(user);
		logUserChange(user, ActionLogEnum.CREATE, null);
		return user;
	}

	@Override
	public User handleGetUserById(long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return user;
	}

	@Override
	public ResultPaginationDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {
		Page<User> pageUser = this.userRepository.findAll(spec, pageable);
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
	@Transactional
	public User handleUpdateUser(UserReqDTO userUpdate) {
		User user = this.handleGetUserById(userUpdate.getId());
		
		User oldUser = User.builder()
				.id(user.getId())
				.email(user.getEmail())
				.username(user.getUsername())
				.password(user.getPassword())
				.gender(user.getGender())
				.phone(user.getPhone())
				.address(user.getAddress())
				.avatar(user.getAvatar())
				.enabled(user.isEnabled())
				.build();

		user.setUsername(userUpdate.getUsername());
		user.setGender(GenderEnum.valueOf(userUpdate.getGender()));
		user.setPhone(userUpdate.getPhone());
		user.setAddress(userUpdate.getAddress());
		user.setAvatar(userUpdate.getAvatar());
		user.setEnabled(userUpdate.isEnabled());

		User updatedUser = this.userRepository.save(user);
		
		logUserChange(updatedUser, ActionLogEnum.UPDATE, oldUser);
		
		return updatedUser;
	}

	@Override
	@Transactional
	public void handleDeleteUser(long id) {
		User user = this.handleGetUserById(id);

		userRepository.delete(user);
		logUserChange(user, ActionLogEnum.DELETE, user);
	}

	@Override
	public void logUserChange(User user, ActionLogEnum action, User oldUser) {
		try {
			String oldValue = oldUser != null ? objectMapper.writeValueAsString(oldUser) : null;
			String newValue = objectMapper.writeValueAsString(user);

			UserAuditLog log = UserAuditLog.builder()
					.user(user) 
					.action(action)
					.oldValue(oldValue)
					.newValue(newValue)
					.modifiedBy(user.getLastModifiedBy())
					.modifiedDate(Instant.now())
					.build();

			auditLogRepository.save(log);
		} catch (Exception e) {
			System.err.println("Failed to create audit log: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
