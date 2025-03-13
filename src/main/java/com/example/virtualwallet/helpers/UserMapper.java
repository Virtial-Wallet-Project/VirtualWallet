package com.example.virtualwallet.helpers;

import com.example.virtualwallet.models.*;
import com.example.virtualwallet.service.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private final UserService userService;

    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User dtoToObject(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setBalance(0.0);
        user.setAdmin(false);
        user.setBlocked(false);
        return user;
    }

    public UserDtoOut createDtoOutFromObject(UserDto userDto) {
        UserDtoOut userOut = new UserDtoOut();
        userOut.setUsername(userDto.getUsername());
        userOut.setEmail(userDto.getEmail());
        userOut.setPhoneNumber(userDto.getPhoneNumber());
        return userOut;
    }

    public User createUpdatedUserFromDto(UserUpdateDto userDto, int id) {
        User existingUser = userService.getUserById(id);
        existingUser.setPassword(userDto.getPassword());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        return existingUser;
    }

    public UserDtoOut createDtoOut (User user) {
        UserDtoOut userOut = new UserDtoOut();
        userOut.setUsername(user.getUsername());
        userOut.setEmail(user.getEmail());
        userOut.setPhoneNumber(user.getPhoneNumber());
        return userOut;
    }

    public List<UserDtoOut> allUsersToDtoOut(List<User> userList){
        List<UserDtoOut> userDto = new ArrayList<>();
        for (User user : userList){
            UserDtoOut userDtoOut = new UserDtoOut();
            userDtoOut.setUsername(user.getUsername());
            userDtoOut.setEmail(user.getEmail());
            userDtoOut.setPhoneNumber(user.getPhoneNumber());
            userDto.add(userDtoOut);
        }

        return userDto;
    }

    public UserDtoOutForTransactions userToDtoForTransactions(User user) {
        if (user == null) {
            return null;
        }
        UserDtoOutForTransactions dto = new UserDtoOutForTransactions();
        dto.setUsername(user.getUsername());
        return dto;
    }
}
