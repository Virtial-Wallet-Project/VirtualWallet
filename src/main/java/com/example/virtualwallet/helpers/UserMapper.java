package com.example.virtualwallet.helpers;

import com.example.virtualwallet.models.User;
import com.example.virtualwallet.models.UserDto;
import com.example.virtualwallet.models.UserDtoOut;
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
        user.setPhoneNumber(user.getPhoneNumber());
        user.setAdmin(false);
        user.setBlocked(false);
        return user;
    }

    public UserDtoOut createDtoOutFromObject(UserDto userDto) {
        UserDtoOut userOut = new UserDtoOut();
        userOut.setUsername(userDto.getUsername());
        userOut.setEmail(userDto.getEmail());
        return userOut;
    }

    public User createUpdatedUserFromDto(UserDto userDto, int id) {
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
        return userOut;
    }

    public List<UserDtoOut> allUsersToDtoOut(List<User> userList){
        List<UserDtoOut> userDto = new ArrayList<>();
        for (User user : userList){
            UserDtoOut userDtoOut = new UserDtoOut();
            userDtoOut.setUsername(user.getUsername());
            userDtoOut.setEmail(user.getEmail());
            userDto.add(userDtoOut);
        }

        return userDto;
    }
}
