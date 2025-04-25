package com.vn.fruitcart.controller;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.request.LoginReqDTO;
import com.vn.fruitcart.domain.dto.response.LoginResDTO;
import com.vn.fruitcart.domain.dto.response.LoginResDTO.UserLogin;
import com.vn.fruitcart.domain.dto.response.UserResDTO;
import com.vn.fruitcart.exception.ResourceAlreadyExistsException;
import com.vn.fruitcart.service.UserService;
import com.vn.fruitcart.util.SecurityUtil;
import com.vn.fruitcart.util.annotation.ApiMessage;
import com.vn.fruitcart.util.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SecurityUtil securityUtil;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthController(
      AuthenticationManagerBuilder authenticationManagerBuilder,
      SecurityUtil securityUtil,
      UserService userService,
      PasswordEncoder passwordEncoder) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.securityUtil = securityUtil;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenExpiration;

  @PostMapping("/login")
  public ResponseEntity<LoginResDTO> login(@Valid @RequestBody LoginReqDTO loginDto) {
    // Nạp input gồm username/password vào Security
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDto.getUsername(), loginDto.getPassword());

    // xác thực người dùng => cần viết hàm loadUserByUsername
    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);

    // set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
    SecurityContextHolder.getContext().setAuthentication(authentication);

    LoginResDTO res = new LoginResDTO();
    User currentUserDB = this.userService.handleGetUserByEmail(loginDto.getUsername());
    if (currentUserDB != null) {
      LoginResDTO.UserLogin userLogin = new UserLogin(
          currentUserDB.getId(),
          currentUserDB.getEmail(),
          currentUserDB.getUsername(),
          currentUserDB.getRole());
      res.setUser(userLogin);
    }

    // create access token
    String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
    res.setAccessToken(access_token);

    // create refresh token
    String refresh_token = this.securityUtil.createRefreshToken(loginDto.getUsername(), res);

    // update user
    this.userService.updateUserToken(refresh_token, loginDto.getUsername());

    // set cookies
    ResponseCookie resCookies = ResponseCookie
        .from("refresh_token", refresh_token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiration)
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, resCookies.toString())
        .body(res);
  }

  @PostMapping("/register")
  @ApiMessage("Register a new user")
  public ResponseEntity<UserResDTO> register(@Valid @RequestBody User postManUser) {
    boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
    if (isEmailExist) {
      throw new ResourceAlreadyExistsException(
          "Email " + postManUser.getEmail() + "đã tồn tại, vui lòng sử dụng email khác.");
    }

    String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
    postManUser.setPassword(hashPassword);
    User user = this.userService.handleCreateUser(postManUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResUser(user));
  }
}
