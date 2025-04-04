package com.vn.fruitcart.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.request.UserReqDTO;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.util.constant.ActionLogEnum;

public interface UserService {
	User handleCreateUser(User user);

	User handleGetUserById(long id);

	ResultPaginationDTO handleGetAllUsers(Specification<User> spec, Pageable pageable);

	List<User> handleGetAllUsers();

	User handleUpdateUser(UserReqDTO userUpdate);

	void handleDeleteUser(long id);

	void logUserChange(User user, ActionLogEnum action, User oldUser);
}
