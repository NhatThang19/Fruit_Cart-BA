package com.vn.fruitcart.service;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.request.LoginReqDTO;
import com.vn.fruitcart.domain.dto.response.LoginResDTO;
import org.springframework.http.ResponseCookie;

public interface AuthService {

  LoginResDTO authenticate(LoginReqDTO loginDTO);

  ResponseCookie createRefreshTokenCookie(String refreshToken);

  LoginResDTO handleGetRefreshToken(String refreshToken);

  void handleLogout(String email);

  ResponseCookie createLogoutCookie();

  User registerUser(User user);
}
