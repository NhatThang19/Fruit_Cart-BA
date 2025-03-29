package com.vn.fruitcart.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReqDTO {
	private long id;
	
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	private String username;
	
	@Pattern(regexp = "(^$|[0-9]{10,15})", message = "Phone number must be 10-15 digits")
	private String phone;
	
	private String address;
	private String avatar;
	
	private String gender;
	
	@NotNull(message = "Enabled cannot be null")
	private boolean enabled;
}
