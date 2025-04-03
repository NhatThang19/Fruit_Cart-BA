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
import com.vn.fruitcart.util.constans.GenderEnum;
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
		this.objectMapper =  objectMapper;
	}

	@Override
	public User handleCreateUser(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		User savedUser = this.userRepository.save(user);  // Lưu và lấy user đã được persist
		logUserChange(savedUser, "CREATE", null);
		return savedUser;
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
		// Lấy user hiện tại từ database
		User existingUser = this.handleGetUserById(userUpdate.getId());
		
		// Lưu bản sao của existingUser để log
		User oldUser = User.builder()
				.id(existingUser.getId())
				.username(existingUser.getUsername())
				.gender(existingUser.getGender())
				.phone(existingUser.getPhone())
				.address(existingUser.getAddress())
				.avatar(existingUser.getAvatar())
				.enabled(existingUser.isEnabled())
				.build();

		// Cập nhật thông tin mới
		existingUser.setUsername(userUpdate.getUsername());
		existingUser.setGender(GenderEnum.valueOf(userUpdate.getGender()));
		existingUser.setPhone(userUpdate.getPhone());
		existingUser.setAddress(userUpdate.getAddress());
		existingUser.setAvatar(userUpdate.getAvatar());
		existingUser.setEnabled(userUpdate.isEnabled());

		// Lưu thay đổi
		User updatedUser = this.userRepository.save(existingUser);
		
		// Log thay đổi với oldUser là bản ghi cũ, updatedUser là bản ghi mới
		logUserChange(updatedUser, "UPDATE", oldUser);
		
		return updatedUser;
	}

	@Override
	public void handleDeleteUser(long id) {
		User existingUser = this.handleGetUserById(id);

		userRepository.delete(existingUser);
		logUserChange(existingUser, "DELETE", existingUser);
	}

	@Override
	public void logUserChange(User user, String action, User oldUser) {
		try {
			String oldValue = oldUser != null ? objectMapper.writeValueAsString(oldUser) : null;
			String newValue = objectMapper.writeValueAsString(user);

			UserAuditLog log = UserAuditLog.builder()
					.userId(user.getId())
					.action(action)
					.oldValue(oldValue)
					.newValue(newValue)
					.modifiedBy(user.getLastModifiedBy())
					.modifiedDate(Instant.now())
					.build();

			auditLogRepository.save(log);
		} catch (Exception e) {
			System.err.println("Failed to create audit log: " + e.getMessage());
		}
	}
}
