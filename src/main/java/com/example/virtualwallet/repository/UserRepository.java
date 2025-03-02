package com.example.virtualwallet.repository;

import com.example.virtualwallet.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByFirstName(String firstName);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
