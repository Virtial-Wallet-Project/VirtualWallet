package com.example.virtualwallet.helpers;

import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserRepository userRepository;

    @Autowired
    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUpdatedUserFromDto(User user, int id) {
        User existingUser = userRepository.getById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        return existingUser;
    }
}
