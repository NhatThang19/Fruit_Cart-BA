package com.vn.fruitcart.util.mapper;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.request.UserReqDTO;
import com.vn.fruitcart.domain.dto.response.UserResDTO;
import com.vn.fruitcart.util.constant.GenderEnum;

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
			.gender(user.getGender() != null ? user.getGender().name() : null)
			.createdBy(user.getCreatedBy())
			.createdDate(user.getCreatedDate())
			.lastModifiedBy(user.getLastModifiedBy())
			.lastModifiedDate(user.getLastModifiedDate())
			.build();
	}
	
	public static User toUser(UserReqDTO userUpdate) {
		return User.builder()
			.id(userUpdate.getId())
			.username(userUpdate.getUsername())
			.phone(userUpdate.getPhone())
			.address(userUpdate.getAddress())
			.avatar(userUpdate.getAvatar())
			.enabled(userUpdate.isEnabled())
			.gender(userUpdate.getGender() != null ? GenderEnum.valueOf(userUpdate.getGender()) : null)
			.build();
	}
}
