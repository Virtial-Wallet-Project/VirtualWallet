package com.example.virtualwallet.repositories;

import com.example.virtualwallet.filtering.FilterUserOptions;
import com.example.virtualwallet.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll(FilterUserOptions filterOptions, int page, int size);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber (String phoneNumber);

    User getByUsernameOrEmailOrPhone(String identifier);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
