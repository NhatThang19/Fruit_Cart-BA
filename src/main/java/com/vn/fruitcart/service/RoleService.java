package com.vn.fruitcart.service;

import com.vn.fruitcart.domain.Role;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {

  boolean existByName(String name);

  Role handleCreateRole(Role r);

  Role handleUpdateRole(Role r);

  void handleDeleteRole(long id);

  Role handleGetRoleById(long id);

  ResultPaginationDTO handleGetAllRole(Specification<Role> spec, Pageable pageable);
}
