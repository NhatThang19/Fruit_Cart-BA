package com.vn.fruitcart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {

  private static final String[] WHITE_LIST_ENDPOINTS = {
      "/",
      "/api/auth/**",
      "/api/files",
  };

  @Bean
  public PermissionInterceptor permissionInterceptor() {
    return new PermissionInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(permissionInterceptor())
        .excludePathPatterns(WHITE_LIST_ENDPOINTS);
  }
}
