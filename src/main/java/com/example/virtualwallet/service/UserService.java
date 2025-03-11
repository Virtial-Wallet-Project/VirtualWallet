package com.example.virtualwallet.service;

import com.example.virtualwallet.models.FilterUserOptions;
import com.example.virtualwallet.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll(FilterUserOptions filterUserOptions, int page, int size, User user);

    User getById(User admin, int id);

    User getUserById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    void createUser(User user);

    void updateUser(User user, User modifier);

    void deleteUser(User admin, int id);

    void makeAdmin(User admin, int id);

    void removeAdmin(User admin, int id);

    void blockUser(User admin, int id);

    void unblockUser(User admin, int id);
}
