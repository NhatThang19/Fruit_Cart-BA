package com.vn.fruitcart.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vn.fruitcart.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LoginResDTO {

  @JsonProperty("access_token")
  private String accessToken;

  private UserLogin user;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserLogin {

    private long id;
    private String email;
    private String name;
    private Role role;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserInsideToken {

    private long id;
    private String email;
    private String name;
  }
}
