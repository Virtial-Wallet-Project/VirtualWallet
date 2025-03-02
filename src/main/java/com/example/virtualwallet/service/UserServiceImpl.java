package com.example.virtualwallet.service;

import com.example.virtualwallet.exception.DuplicateEntityException;
import com.example.virtualwallet.exception.EntityNotFoundException;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getById(User modifier, int id) {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User getByUsernameForAdmin(User admin, String username) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(User admin, String email) {
        return null;
    }

    @Override
    public User getByFirstName(User admin, String firstName) {
        return null;
    }

    @Override
    public void createUser(User user) {
        boolean usernameExists = true;
        boolean emailExists = true;
        try {
            userRepository.getByUsername(user.getUsername());

        } catch (EntityNotFoundException e) {
            usernameExists = false;


        } try {
            userRepository.getByEmail(user.getEmail());

        } catch (EntityNotFoundException e) {
            emailExists = false;
        }


        if (usernameExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());

        } else if (emailExists){
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

        userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user, User modifier) {

    }

    @Override
    public void deleteUser(User modifier, int id) {

    }

    @Override
    public void makeAdmin(User admin, int id) {

    }

    @Override
    public void removeAdmin(User admin, int id) {

    }

    @Override
    public void blockUser(User admin, int id) {

    }

    @Override
    public void unblockUser(User admin, int id) {

    }
}
