package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.DTOs.UserDto;
import com.example.virtualwallet.DTOs.UserDtoOut;
import com.example.virtualwallet.DTOs.UserUpdateDto;
import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.filtering.FilterUserOptions;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.mappers.UserMapper;
import com.example.virtualwallet.models.*;
import com.example.virtualwallet.service.CreditCardService;
import com.example.virtualwallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Different user related options. From creating profile all the way to Admin related controls.")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authorizationHelper;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper, AuthenticationHelper authorizationHelper,
                              CreditCardService cardService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @GetMapping
    public List<UserDtoOut> getAll(@RequestParam(required = false) String username,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String phoneNumber,
                                   @RequestParam(required = false) String sortBy,
                                   @RequestParam(required = false) String orderBy,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestHeader HttpHeaders headers) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            FilterUserOptions filterOptions = new FilterUserOptions(username, email, phoneNumber, sortBy, orderBy);
            return userMapper.allUsersToDtoOut(userService.getAll(filterOptions, page, size, user));

        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public UserDtoOut getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authorizationHelper.tryGetUser(headers);
            User existingUser = userService.getById(user, id);
            return userMapper.createDtoOut(existingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Get a specific user.", description = "Fetches a user by their unique username.")
    @GetMapping("/username/{username}")
    @SecurityRequirement(name = "authHeader")
    public UserDtoOut getUserByUsername(@RequestHeader HttpHeaders headers, @PathVariable String username) {
        try {
            authorizationHelper.tryGetUser(headers);
            User existingUser = userService.getByUsername(username);
            return userMapper.createDtoOut(existingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Get a specific user.", description = "Fetches a user by their unique email.")
    @GetMapping("/email/{email}")
    @SecurityRequirement(name = "authHeader")
    public UserDtoOut getUserByEmail(@RequestHeader HttpHeaders headers, @PathVariable String email) {
        try {
            authorizationHelper.tryGetUser(headers);
            User existingUser = userService.getByEmail(email);
            return userMapper.createDtoOut(existingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Get a specific user.", description = "Fetches a user by their unique phone number.")
    @GetMapping("/phoneNumber/{phoneNumber}")
    @SecurityRequirement(name = "authHeader")
    public UserDtoOut getUserByPhoneNumber(@RequestHeader HttpHeaders headers, @PathVariable String phoneNumber) {
        try {
            authorizationHelper.tryGetUser(headers);
            User existingUser = userService.getByPhoneNumber(phoneNumber);
            return userMapper.createDtoOut(existingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Create a user.", description = "Creates a user after providing personal information.")
    @PostMapping
    public UserDtoOut createUser(@Valid @RequestBody UserDto userDto) {
        try {
            User newUser = userMapper.dtoToObject(userDto);
            userService.createUser(newUser);
            return userMapper.createDtoOutFromObject(userDto);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Operation(summary = "Updates a user's information.", description = "Updates a user's personal info such as first name, last name, email or password.")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "authHeader")
    public UserDtoOut updateUser(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserUpdateDto userDto) {
        try {
            User modifier = authorizationHelper.tryGetUser(headers);
            User user = userMapper.createUpdatedUserFromDto(userDto, id);
            userService.updateUser(user, modifier);

            UserDtoOut userOut = new UserDtoOut();
            userOut.setUsername(userService.getUserById(id).getUsername());
            userOut.setEmail(user.getEmail());
            userOut.setPhoneNumber(user.getPhoneNumber());
            return userOut;

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Deletes a specific user.", description = "Deletes a user's account after passing authorization.")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "authHeader")
    public void deleteUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            userService.deleteUser(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Make a user an admin.", description = "An option where an admin can make another regular user an admin.")
    @PutMapping("/admins/promote/{id}")
    @SecurityRequirement(name = "authHeader")
    public void makeUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            userService.makeAdmin(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Demote a user from being an admin.", description = "An option where an admin can make another admin a regular user.")
    @PutMapping("/admins/demote/{id}")
    @SecurityRequirement(name = "authHeader")
    public void demoteUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            userService.removeAdmin(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Block a user.", description = "An option where an admin can block a specific user.")
    @PutMapping("/admins/block/{id}")
    @SecurityRequirement(name = "authHeader")
    public void blockUser (@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            userService.blockUser(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Unblock a user.", description = "An option where an admin can unblock a specific user.")
    @PutMapping("/admins/unblock/{id}")
    @SecurityRequirement(name = "authHeader")
    public void unblockUser (@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            userService.unblockUser(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
