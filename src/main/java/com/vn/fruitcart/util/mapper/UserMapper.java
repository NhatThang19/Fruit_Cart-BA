package com.vn.fruitcart.util.mapper;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.response.ResUserDTO;

public class UserMapper {
	public static ResUserDTO toResUser(User user) {
		return ResUserDTO.builder()
			.id(user.getId())
			.username(user.getUsername())
			.email(user.getEmail())
			.phone(user.getPhone())
			.address(user.getAddress())
			.avatar(user.getAvatar())
			.enabled(user.isEnabled())
			.build();
	}
	
	public static User toUser(ResUserDTO resUserDTO) {
		return User.builder()
			.id(resUserDTO.getId())
			.username(resUserDTO.getUsername())
			.email(resUserDTO.getEmail())
			.phone(resUserDTO.getPhone())
			.address(resUserDTO.getAddress())
			.avatar(resUserDTO.getAvatar())
			.enabled(resUserDTO.isEnabled())
			.build();
	}
}
