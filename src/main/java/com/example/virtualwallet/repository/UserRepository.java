package com.example.virtualwallet.repository;

import com.example.virtualwallet.models.FilterUserOptions;
import com.example.virtualwallet.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll(FilterUserOptions filterOptions);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
