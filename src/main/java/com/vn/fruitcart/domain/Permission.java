package com.vn.fruitcart.domain;

import com.vn.fruitcart.util.constant.MethodEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vn.fruitcart.domain.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "name không được để trống")
  @Column(nullable = false)
  private String name;

  @NotBlank(message = "apiPath không được để trống")
  @Column(nullable = false)
  private String apiPath;

  @NotNull(message = "method không được để trống")
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MethodEnum method;

  @NotBlank(message = "module không được để trống")
  @Column(nullable = false)
  private String module;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
  @JsonIgnore
  private List<Role> roles;
}
