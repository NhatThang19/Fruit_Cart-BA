package com.vn.fruitcart.service.impl;

import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.repository.PermissionRepository;
import com.vn.fruitcart.service.PermissionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

  private final PermissionRepository permissionRepository;

  public PermissionServiceImpl(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  @Override
  public Permission handleCreatePermission(Permission permission) {
    return this.permissionRepository.save(permission);
  }

  @Override
  public Permission handleGetPermissionById(Long id) {
    return null;
  }

  @Override
  public Permission handleUpdatePermission(Permission updatePermission) {
    return null;
  }

  @Override
  public void handleDeletePermission(Long id) {

  }

  @Override
  public ResultPaginationDTO handleGetAllPermission(Specification<Permission> spec,
      Pageable pageable) {
    return null;
  }

  @Override
  public boolean isPermissionExist(Permission permission) {
    return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(),
        permission.getApiPath(),
        permission.getMethod().toString());
  }
}
