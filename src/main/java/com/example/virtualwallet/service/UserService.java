package com.example.virtualwallet.service;

import com.example.virtualwallet.models.FilterUserOptions;
import com.example.virtualwallet.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll(FilterUserOptions filterUserOptions);

    User getById(User modifier, int id);

    User getUserById(int id);

    User getByUsernameForAdmin(User admin, String username);

    User getByUsername(String username);

    User getByEmail(User admin, String email);

    void createUser(User user);

    void updateUser(User user, User modifier);

    void deleteUser(User modifier, int id);

    void makeAdmin (User admin, int id);

    void removeAdmin (User admin, int id);

    void blockUser (User admin, int id);

    void unblockUser (User admin, int id);
}
