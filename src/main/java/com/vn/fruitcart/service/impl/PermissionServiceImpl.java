package com.vn.fruitcart.service.impl;

import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.exception.ResourceAlreadyExistsException;
import com.vn.fruitcart.exception.ResourceNotFoundException;
import com.vn.fruitcart.repository.PermissionRepository;
import com.vn.fruitcart.service.PermissionService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
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
  @Transactional
  public Permission handleCreatePermission(Permission permission) {
    boolean isPExist = this.isPermissionExist(permission);
    if (isPExist) {
      throw new ResourceAlreadyExistsException("Quyền '" + permission.getName() + "' Đã tồn tại");
    }
    return this.permissionRepository.save(permission);
  }

  @Override
  public Permission handleGetPermissionById(Long id) {
    return this.permissionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quyền với id: " + id));
  }

  @Override
  @Transactional
  public Permission handleUpdatePermission(Permission updatePermission) {
    Permission p = this.handleGetPermissionById(updatePermission.getId());

    p.setName(updatePermission.getName());
    p.setModule(updatePermission.getModule());
    p.setMethod(updatePermission.getMethod());
    p.setApiPath(updatePermission.getApiPath());

    return this.permissionRepository.save(p);
  }

  @Override
  public void handleDeletePermission(Long id) {
    this.permissionRepository.delete(this.handleGetPermissionById(id));
  }

  @Override
  public ResultPaginationDTO handleGetAllPermission(Specification<Permission> spec,
      Pageable pageable) {
    Page<Permission> pageP = this.permissionRepository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());
    mt.setPages(pageP.getTotalPages());
    mt.setTotal(pageP.getTotalElements());

    rs.setMeta(mt);
    rs.setResult(pageP);

    return rs;
  }

  @Override
  public boolean isPermissionExist(Permission permission) {
    return this.permissionRepository.existsByModuleAndApiPathAndMethod(
        permission.getModule(),
        permission.getApiPath(),
        permission.getMethod());
  }
}
