package com.example.virtualwallet.helpers;

import com.example.virtualwallet.exceptions.AuthenticationFailureException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    public static final String AUTHENTICATION_FAILURE = "Wrong username or password! Please try again.";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = userService.getByUsername(username);

            if (!user.getPassword().equals(password)) {
                throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }
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

    public User tryGetUser(HttpSession httpSession){
        String currentUser = (String) httpSession.getAttribute("currentUser");

        if (currentUser == null) {
            throw new AuthenticationFailureException("No user logged in.");
        }

        return userService.getByUsername(currentUser);
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpace + 1);
    }
}
