package com.vn.fruitcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.fruitcart.domain.UserAuditLog;

@Repository
public interface UserAuditLogRepository extends JpaRepository<UserAuditLog, Long> {
}
