package com.vn.fruitcart.repository;

import com.vn.fruitcart.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

  boolean existsByName(String name);

  Role findByName(String name);
}
