package com.vn.fruitcart.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.fruitcart.domain.Role;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.service.RoleService;
import com.vn.fruitcart.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @ApiMessage("Create new role")
  @PostMapping
  public ResponseEntity<Role> createRole(@Valid @RequestBody Role r) {
    Role newR = this.roleService.handleCreateRole(r);
    return ResponseEntity.status(HttpStatus.CREATED).body(newR);
  }

  @ApiMessage("Update role")
  @PutMapping
  public ResponseEntity<Role> updateRole(@Valid @RequestBody Role r) {
    Role updateR = this.roleService.handleUpdateRole(r);
    return ResponseEntity.ok().body(updateR);
  }

  @ApiMessage("Get role by id")
  @GetMapping("/{id}")
  public ResponseEntity<Role> getRoleById(@PathVariable long id) {
    Role r = this.roleService.handleGetRoleById(id);
    return ResponseEntity.ok().body(r);
  }

  @ApiMessage("Delete role")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRole(@PathVariable long id) {
    this.roleService.handleDeleteRole(id);
    return ResponseEntity.ok(null);
  }

  @GetMapping()
  @ApiMessage("Get all role")
  public ResponseEntity<ResultPaginationDTO> getAllPermission(
      @Filter Specification<Role> spec,
      Pageable pageable) {
    return ResponseEntity.ok()
        .body(this.roleService.handleGetAllRole(spec, pageable));
  }
}
