package com.vn.fruitcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = {
//    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//    org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class})
@SpringBootApplication
public class FruitcartApplication {

  public static void main(String[] args) {
    SpringApplication.run(FruitcartApplication.class, args);
  }

}
