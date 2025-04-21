package com.vn.fruitcart.controller;

import com.turkraft.springfilter.boot.Filter;
import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.domain.User;
import com.vn.fruitcart.domain.dto.response.ResultPaginationDTO;
import com.vn.fruitcart.service.PermissionService;
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
@RequestMapping("/api/permissions")
public class PermissionController {

  private final PermissionService permissionService;

  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @ApiMessage("Create new permission")
  @PostMapping
  public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission p) {
    Permission newP = this.permissionService.handleCreatePermission(p);
    return ResponseEntity.status(HttpStatus.CREATED).body(newP);
  }

  @ApiMessage("Update permission")
  @PutMapping
  public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission p) {
    Permission updateP = this.permissionService.handleUpdatePermission(p);
    return ResponseEntity.ok().body(updateP);
  }

  @ApiMessage("Get permission by id")
  @GetMapping("/{id}")
  public ResponseEntity<Permission> getPermissionById(@PathVariable long id) {
    Permission p = this.permissionService.handleGetPermissionById(id);
    return ResponseEntity.ok().body(p);
  }

  @ApiMessage("Delete permission")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePermission(@PathVariable long id) {
    this.permissionService.handleDeletePermission(id);
    return ResponseEntity.ok(null);
  }

  @GetMapping()
  @ApiMessage("Get all permission")
  public ResponseEntity<ResultPaginationDTO> getAllPermission(
      @Filter Specification<Permission> spec,
      Pageable pageable) {
    return ResponseEntity.ok()
        .body(this.permissionService.handleGetAllPermission(spec, pageable));
  }


}
