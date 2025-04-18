package com.vn.fruitcart.repository;

import com.vn.fruitcart.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>,
    JpaSpecificationExecutor<Permission> {

  boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);
}
