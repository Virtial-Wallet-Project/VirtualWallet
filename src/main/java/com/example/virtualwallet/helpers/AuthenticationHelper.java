package com.example.virtualwallet.helpers;

import com.example.virtualwallet.exception.AuthenticationFailureException;
import com.example.virtualwallet.exception.EntityNotFoundException;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    public static final String AUTHENTICATION_FAILURE = "Wrong username or password! Please try again.";


    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getByUsername(username);

            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE);
            }

            return user;

        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE);
        }
    }
}
