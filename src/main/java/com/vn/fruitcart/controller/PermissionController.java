package com.vn.fruitcart.controller;

import com.vn.fruitcart.domain.Permission;
import com.vn.fruitcart.service.PermissionService;
import com.vn.fruitcart.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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


}
