package com.vn.fruitcart.util.mapper;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.response.UserResDTO;

public class UserMapper {
	public static UserResDTO toResUser(User user) {
		return UserResDTO.builder()
			.id(user.getId())
			.username(user.getUsername())
			.email(user.getEmail())
			.phone(user.getPhone())
			.address(user.getAddress())
			.avatar(user.getAvatar())
			.enabled(user.isEnabled())
			.build();
	}
	
	public static User toUser(UserResDTO UserResDTO) {
		return User.builder()
			.id(UserResDTO.getId())
			.username(UserResDTO.getUsername())
			.email(UserResDTO.getEmail())
			.phone(UserResDTO.getPhone())
			.address(UserResDTO.getAddress())
			.avatar(UserResDTO.getAvatar())
			.enabled(UserResDTO.isEnabled())
			.build();
	}
}
