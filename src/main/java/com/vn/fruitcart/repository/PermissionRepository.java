package com.vn.fruitcart.repository;

import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.util.constant.MethodEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>,
    JpaSpecificationExecutor<Permission> {

  boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, MethodEnum method);

  List<Permission> findByIdIn(List<Long> reqPermissions);
}
