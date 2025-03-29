package com.vn.fruitcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.fruitcart.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
