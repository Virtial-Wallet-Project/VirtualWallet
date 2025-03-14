package com.example.virtualwallet.mappers;

import com.example.virtualwallet.DTOs.RegisterDto;
import com.example.virtualwallet.models.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    public User fromDto(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());

        return user;
    }
}
