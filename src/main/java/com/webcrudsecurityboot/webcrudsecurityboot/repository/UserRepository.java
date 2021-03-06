package com.webcrudsecurityboot.webcrudsecurityboot.repository;

import com.webcrudsecurityboot.webcrudsecurityboot.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    User show(Long id);
    void save(User user);
    void update(Long id, User updatedUser);
    void delete(Long id);
    User findByName(String name);
}
