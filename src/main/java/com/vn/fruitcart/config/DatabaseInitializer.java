package com.vn.fruitcart.config;

import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.domain.Role;
import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.repository.PermissionRepository;
import com.vn.fruitcart.repository.RoleRepository;
import com.vn.fruitcart.repository.UserRepository;
import com.vn.fruitcart.util.constant.GenderEnum;
import com.vn.fruitcart.util.constant.MethodEnum;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializer implements CommandLineRunner {

  private final PermissionRepository permissionRepository;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public DatabaseInitializer(
      PermissionRepository permissionRepository,
      RoleRepository roleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.permissionRepository = permissionRepository;
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Value("${admin.password}")
  private String adminPassword;

  @Override
  public void run(String... args) throws Exception {
    System.out.println(">>> START INIT DATABASE");
    long countPermissions = this.permissionRepository.count();
    long countRoles = this.roleRepository.count();
    long countUsers = this.userRepository.count();

    if (countPermissions == 0) {
      ArrayList<Permission> arr = new ArrayList<>();
      arr.add(new Permission("Tạo người dùng", "/api/v1/users", MethodEnum.POST, "USERS"));
      arr.add(new Permission("Sửa người dùng", "/api/v1/users", MethodEnum.PUT, "USERS"));
      arr.add(new Permission("Xoá người dùng", "/api/v1/users", MethodEnum.DELETE, "USERS"));
      arr.add(new Permission("Lấy người dùng bởi id", "/api/v1/users", MethodEnum.GET, "USERS"));
      arr.add(new Permission("Lấy người dùng với phân trang", "/api/v1/users", MethodEnum.GET,
          "USERS"));

      this.permissionRepository.saveAll(arr);
    }

    if (countRoles == 0) {
      List<Permission> allPermissions = this.permissionRepository.findAll();

      Role adminRole = new Role();
      adminRole.setName("SUPER_ADMIN");
      adminRole.setDescription("Admin thì full permissions");
      adminRole.setPermissions(allPermissions);

      this.roleRepository.save(adminRole);
    }

    if (countUsers == 0) {
      User adminUser = new User();
      adminUser.setEmail("admin@gmail.com");
      adminUser.setGender(GenderEnum.MALE);
      adminUser.setUsername("ADMIN");
      adminUser.setPassword(this.passwordEncoder.encode(adminPassword));

      Role adminRole = this.roleRepository.findByName("ADMIN");
      if (adminRole != null) {
        adminUser.setRole(adminRole);
      }

      this.userRepository.save(adminUser);
    }

    if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
      System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
    } else {
      System.out.println(">>> END INIT DATABASE");
    }
  }
}
