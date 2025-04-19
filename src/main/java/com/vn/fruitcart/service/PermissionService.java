package com.vn.fruitcart.service;

import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PermissionService {

  Permission handleCreatePermission(Permission permission);

  Permission handleGetPermissionById(Long id);

  Permission handleUpdatePermission(Permission updatePermission);

  void handleDeletePermission(Long id);

  ResultPaginationDTO handleGetAllPermission(Specification<Permission> spec, Pageable pageable);

  boolean isPermissionExist(Permission permission);
}
