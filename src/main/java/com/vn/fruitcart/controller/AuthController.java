package com.vn.fruitcart.controller;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.request.LoginReqDTO;
import com.vn.fruitcart.domain.dto.response.LoginResDTO;
import com.vn.fruitcart.domain.dto.response.UserResDTO;
import com.vn.fruitcart.service.AuthService;
import com.vn.fruitcart.util.SecurityUtil;
import com.vn.fruitcart.util.annotation.ApiMessage;
import com.vn.fruitcart.util.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(
      AuthService authService) {
    this.authService = authService;
  }

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenExpiration;

  @PostMapping("/login")
  @ApiMessage("Login user")
  public ResponseEntity<LoginResDTO> login(@Valid @RequestBody LoginReqDTO loginDto) {
    LoginResDTO res = authService.authenticate(loginDto);
    ResponseCookie cookie = authService.createRefreshTokenCookie(res.getRefreshToken());
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(res);
  }

  @GetMapping("/refresh")
  @ApiMessage("Get User by refresh token")
  public ResponseEntity<LoginResDTO> getRefreshToken(
      @CookieValue(name = "refresh_token", required = false) String refresh_token) {
    LoginResDTO res = authService.handleGetRefreshToken(refresh_token);
    ResponseCookie cookie = authService.createRefreshTokenCookie(res.getRefreshToken());
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(res);

  }

  @PostMapping("/logout")
  @ApiMessage("Logout User")
  public ResponseEntity<Void> logout() {
    String email =
        SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
            : "";

    this.authService.handleLogout(email);

    ResponseCookie cookie = authService.createLogoutCookie();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .build();
  }

  @PostMapping("/register")
  @ApiMessage("Register a new user")
  public ResponseEntity<UserResDTO> register(@Valid @RequestBody User userReq) {
    User user = authService.registerUser(userReq);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(UserMapper.toResUser(user));
  }
}
