package com.vn.fruitcart.domain.dto.response;

import java.time.Instant;

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
	private String gender;
	private boolean enabled;
	private String createdBy;
	private Instant createdDate;
	private String lastModifiedBy;
	private Instant lastModifiedDate;
}
