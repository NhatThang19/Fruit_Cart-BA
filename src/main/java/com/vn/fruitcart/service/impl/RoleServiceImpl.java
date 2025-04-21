package com.vn.fruitcart.service.impl;

import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.domain.Role;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.exception.ResourceAlreadyExistsException;
import com.vn.fruitcart.exception.ResourceNotFoundException;
import com.vn.fruitcart.repository.PermissionRepository;
import com.vn.fruitcart.repository.RoleRepository;
import com.vn.fruitcart.service.RoleService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  private final PermissionRepository permissionRepository;

  public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
  }

  @Override
  public boolean existByName(String name) {
    return this.roleRepository.existsByName(name);
  }

  @Override
  public Role handleCreateRole(Role r) {
    if (this.existByName(r.getName())) {
      throw new ResourceAlreadyExistsException("Role với name = " + r.getName() + " đã tồn tại");
    }

    if (r.getPermissions() != null) {
      List<Long> reqPermissions = r.getPermissions()
          .stream().map(Permission::getId)
          .collect(Collectors.toList());

      List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
      r.setPermissions(dbPermissions);
    }

    return this.roleRepository.save(r);
  }

  @Override
  public Role handleUpdateRole(Role r) {
    Role roleDB = this.handleGetRoleById(r.getId());

    if (r.getPermissions() != null) {
      List<Long> reqPermissions = r.getPermissions()
          .stream().map(Permission::getId)
          .collect(Collectors.toList());

      List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
      r.setPermissions(dbPermissions);
    }

    roleDB.setName(r.getName());
    roleDB.setDescription(r.getDescription());
    roleDB.setEnabled(r.isEnabled());
    roleDB.setPermissions(r.getPermissions());
    roleDB = this.roleRepository.save(roleDB);
    return roleDB;
  }

  @Override
  public void handleDeleteRole(long id) {
    this.roleRepository.delete(this.handleGetRoleById(id));
  }

  @Override
  public Role handleGetRoleById(long id) {
    return this.roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vai trò với id: " + id));
  }

  @Override
  public ResultPaginationDTO handleGetAllRole(Specification<Role> spec, Pageable pageable) {
    Page<Role> pageP = this.roleRepository.findAll(spec, pageable);
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

}
