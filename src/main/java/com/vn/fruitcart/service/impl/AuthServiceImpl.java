package com.vn.fruitcart.service.impl;

import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.request.LoginReqDTO;
import com.vn.fruitcart.domain.dto.response.LoginResDTO;
import com.vn.fruitcart.domain.dto.response.LoginResDTO.UserLogin;
import com.vn.fruitcart.exception.InvalidTokenException;
import com.vn.fruitcart.exception.MissingTokenException;
import com.vn.fruitcart.exception.ResourceAlreadyExistsException;
import com.vn.fruitcart.exception.UnauthorizedException;
import com.vn.fruitcart.service.AuthService;
import com.vn.fruitcart.service.UserService;
import com.vn.fruitcart.util.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SecurityUtil securityUtil;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthServiceImpl(
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


  @Override
  public LoginResDTO authenticate(LoginReqDTO LoginDTO) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        LoginDTO.getUsername(), LoginDTO.getPassword());

    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    User currentUserDB = userService.handleGetUserByEmail(LoginDTO.getUsername());
    LoginResDTO res = new LoginResDTO();
    if (currentUserDB != null) {
      LoginResDTO.UserLogin userLogin = new UserLogin(
          currentUserDB.getId(),
          currentUserDB.getEmail(),
          currentUserDB.getUsername(),
          currentUserDB.getRole());
      res.setUser(userLogin);
    }

    String accessToken = securityUtil.createAccessToken(authentication.getName(), res);
    res.setAccessToken(accessToken);

    String refreshToken = securityUtil.createRefreshToken(LoginDTO.getUsername(), res);
    userService.updateUserToken(refreshToken, LoginDTO.getUsername());
    res.setRefreshToken(refreshToken);

    return res;
  }

  @Override
  public ResponseCookie createRefreshTokenCookie(String refreshToken) {
    return ResponseCookie.from("refresh_token", refreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiration)
        .build();
  }

  @Override
  @Transactional
  public LoginResDTO handleGetRefreshToken(String refreshToken) {
    if (refreshToken == null || refreshToken.isEmpty()) {
      throw new MissingTokenException("Không tìm thấy refresh token trong cookie");
    }

    Jwt decodedToken = securityUtil.checkValidRefreshToken(refreshToken);
    String email = decodedToken.getSubject();

    User currentUser = userService.getUserByRefreshTokenAndEmail(refreshToken, email);
    if (currentUser == null) {
      throw new InvalidTokenException("Refresh Token không hợp lệ");
    }

    User currentUserDB = userService.handleGetUserByEmail(email);
    LoginResDTO res = new LoginResDTO();
    if (currentUserDB != null) {
      LoginResDTO.UserLogin userLogin = new UserLogin(
          currentUserDB.getId(),
          currentUserDB.getEmail(),
          currentUserDB.getUsername(),
          currentUserDB.getRole());
      res.setUser(userLogin);
    }

    String accessToken = securityUtil.createAccessToken(email, res);
    res.setAccessToken(accessToken);

    String newRefreshToken = securityUtil.createRefreshToken(email, res);
    userService.updateUserToken(newRefreshToken, email);
    res.setRefreshToken(newRefreshToken);

    return res;
  }

  @Override
  public void handleLogout(String email) {
    if (email == null || email.isEmpty()) {
      throw new UnauthorizedException("Access Token không hợp lệ");
    }
    userService.updateUserToken(null, email);
  }

  @Override
  public ResponseCookie createLogoutCookie() {
    return ResponseCookie.from("refresh_token", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();
  }

  @Override
  public User registerUser(User user) {
    if (userService.isEmailExist(user.getEmail())) {
      throw new ResourceAlreadyExistsException(
          "Email " + user.getEmail() + " đã tồn tại, vui lòng sử dụng email khác.");
    }

    String hashPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(hashPassword);
    return userService.handleCreateUser(user);
  }


}
