package com.vn.fruitcart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vn.fruitcart.domain.User;

public interface UserService {
    User hadnleCreateUser(User user);
    
    Optional<User> handleGetUserById(Long id);
    
    Page<User> handleGetAllUsers(Pageable pageable);
    
    List<User> handleGetAllUsers();
    
    User handleUpdateUser(Long id, User userDetails);
    
    void handleDeleteUser(Long id);
}
