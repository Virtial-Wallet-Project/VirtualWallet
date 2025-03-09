package com.example.virtualwallet.helpers;

import com.example.virtualwallet.models.User;
import com.example.virtualwallet.models.UserUpdateDto;
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

    public User createUpdatedUserFromDto(UserUpdateDto userUpdateDto, int id) {
        User existingUser = userRepository.getById(id);
        existingUser.setUsername(userUpdateDto.getUsername());
        existingUser.setPassword(userUpdateDto.getPassword());
        existingUser.setEmail(userUpdateDto.getEmail());
        existingUser.setPhoneNumber(userUpdateDto.getPhoneNumber());
        return existingUser;
    }
}
