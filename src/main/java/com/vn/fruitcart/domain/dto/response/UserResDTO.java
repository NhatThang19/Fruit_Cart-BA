package com.vn.fruitcart.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResDTO {
	private long id;
	private String username;
	private String email;
	private String phone;
	private String address;
	private String avatar;
	private boolean enabled;
}
