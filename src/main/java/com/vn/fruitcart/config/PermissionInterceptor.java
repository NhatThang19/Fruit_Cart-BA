package com.vn.fruitcart.config;

import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.domain.Role;
import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.exception.PermissionException;
import com.vn.fruitcart.service.UserService;
import com.vn.fruitcart.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;
import java.util.Optional;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);
  private static final String PERMISSION_DENIED_MSG = "Bạn không có quyền truy cập endpoint này.";

  @Autowired
  private UserService userService;

  @Override
  @Transactional(readOnly = true)
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response,
      Object handler) throws Exception {

    String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    String httpMethod = request.getMethod();

    logger.debug("Checking permissions for path: {}, method: {}", path, httpMethod);

    Optional<String> emailOptional = SecurityUtil.getCurrentUserLogin();
    if (emailOptional.isEmpty()) {
      return true;
    }

    String email = emailOptional.get();
    User user = userService.handleGetUserByEmail(email);
    if (user == null) {
      return true;
    }

    Role role = user.getRole();
    if (role == null) {
      throw new PermissionException(PERMISSION_DENIED_MSG);
    }

    if (!hasPermission(role.getPermissions(), path, httpMethod)) {
      throw new PermissionException(PERMISSION_DENIED_MSG);
    }

    return true;
  }

  private boolean hasPermission(List<Permission> permissions, String path, String method) {
    for (Permission permission : permissions) {
      if (permission.getApiPath().equals(path)
          && permission.getMethod().equals(method)) {
        return true;
      }
    }
    return false;
  }
}