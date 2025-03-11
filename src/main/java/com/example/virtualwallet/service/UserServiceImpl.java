package com.example.virtualwallet.service;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.helpers.PermissionHelpers;
import com.example.virtualwallet.models.FilterUserOptions;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll(FilterUserOptions filterUserOptions, int page, int size, User user) {
        PermissionHelpers.checkIfBlocked(user);
        PermissionHelpers.checkIfAdmin(user);
        return userRepository.getAll(filterUserOptions, page, size);
    }

    @Override
    public User getById(User user, int id) {
        PermissionHelpers.checkIfBlocked(user);
        PermissionHelpers.checkIfAdmin(user);
        return userRepository.getById(id);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public void createUser(User user) {
        boolean usernameExists = true;
        boolean emailExists = true;
        boolean phoneNumberExists = true;

        try {
            userRepository.getByUsername(user.getUsername());

        } catch (EntityNotFoundException e) {
            usernameExists = false;


        } try {
            userRepository.getByEmail(user.getEmail());

        } catch (EntityNotFoundException e) {
            emailExists = false;
        }


        try {
            userRepository.getByPhoneNumber(user.getPhoneNumber());

        } catch (EntityNotFoundException e) {
            phoneNumberExists = false;
        }


        if (usernameExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());

        } else if (emailExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());

        } else if (phoneNumberExists) {
            throw new DuplicateEntityException("User", "phone number", user.getPhoneNumber());
        }

        userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user, User modifier) {
        PermissionHelpers.checkIfCreatorOrAdmin(user.getUserId(), modifier);

        boolean duplicateExistsForEmail = true;
        boolean duplicateExistsForPhone = true;

        try {
            User existingUser = userRepository.getByEmail(user.getEmail());

            if (user.getPassword().equals(existingUser.getPassword()) &&
                    user.getEmail().equals(existingUser.getEmail()) &&
                    user.getPhoneNumber().equals(existingUser.getPhoneNumber())) {

                throw new InvalidOperationException("Invalid operation! Nothing was changed!");
            }

            if (existingUser.getUserId() == user.getUserId()) {
                duplicateExistsForEmail = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExistsForEmail = false;

        }

        try {
            User existingUser = userRepository.getByPhoneNumber(user.getPhoneNumber());

            if (existingUser.getUserId() == user.getUserId()) {
                duplicateExistsForPhone = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExistsForPhone = false;
        }

        if (duplicateExistsForEmail) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        } else if (duplicateExistsForPhone) {
            throw new DuplicateEntityException("User", "phone number", user.getPhoneNumber());
        }

        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(User modifier, int id) {
        PermissionHelpers.checkIfCreatorOrAdmin(id, modifier);
        userRepository.deleteUser(id);
    }

    @Override
    public void makeAdmin(User admin, int id) {
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        User user = userRepository.getById(id);
        if (user.isAdmin()){
            throw new InvalidOperationException("User is already admin!");
        }
        user.setAdmin(true);
        userRepository.updateUser(user);
    }

    @Override
    public void removeAdmin(User admin, int id) {
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        User user = userRepository.getById(id);
        if (!user.isAdmin()){
            throw new InvalidOperationException("User is already an ordinary user!");
        }
        user.setAdmin(false);
        userRepository.updateUser(user);
    }

    @Override
    public void blockUser(User admin, int id) {
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        User user = userRepository.getById(id);
        if (user.isBlocked()){
            throw new InvalidOperationException("User is already blocked!");
        }
        user.setBlocked(true);
        userRepository.updateUser(user);
    }

    @Override
    public void unblockUser(User admin, int id) {
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        User user = userRepository.getById(id);
        if (!user.isBlocked()){
            throw new InvalidOperationException("User is already not blocked!");
        }
        user.setBlocked(false);
        userRepository.updateUser(user);
    }
}
