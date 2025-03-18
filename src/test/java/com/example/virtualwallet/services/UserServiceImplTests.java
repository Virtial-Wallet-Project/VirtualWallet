package com.example.virtualwallet.services;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.filtering.FilterUserOptions;
import com.example.virtualwallet.helpers.PermissionHelpers;
import com.example.virtualwallet.helpers.TokenGenerator;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.service.EmailVerificationService;
import com.example.virtualwallet.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.virtualwallet.Helpers.createAnotherMockUser;
import static com.example.virtualwallet.Helpers.createMockUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailVerificationService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PermissionHelpers permissionHelpers;

    private User admin;
    private User user;

    @BeforeEach
    void setUp() {
        admin = new User();
        admin.setUserId(1);
        admin.setAdmin(true);

        user = new User();
        user.setUserId(2);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    public void getAll_ShouldThrowException_WhenUserIsBlocked() {
        FilterUserOptions filterUserOptions = new FilterUserOptions("username", "email", "0897671242",
                "sortBy", "sortOrder");
        int page = 1;
        int size = 2;

        User mockUser = createMockUser();
        mockUser.setBlocked(true);

        assertThrows(UnauthorizedOperationException.class, () -> {
            userService.getAll(filterUserOptions, page, size, mockUser);
        });
    }

    @Test
    public void getAll_ShouldThrowException_WhenUserIsNotAnAdmin() {
        FilterUserOptions filterUserOptions = new FilterUserOptions("username", "email", "0897671242",
                "sortBy", "sortOrder");
        int page = 1;
        int size = 2;

        User mockUser = createMockUser();

        assertThrows(UnauthorizedOperationException.class, () -> {
            userService.getAll(filterUserOptions, page, size, mockUser);
        });
    }


    @Test
    public void getAll_ShouldReturnAllUsers_WhenValid() {
        FilterUserOptions filterUserOptions = new FilterUserOptions("username", "email", "0897671242",
                "sortBy", "sortOrder");
        int page = 1;
        int size = 2;

        User user = createMockUser();
        user.setAdmin(true);

        List<User> allUsers = userService.getAll(filterUserOptions, page, size, user);

        Assertions.assertEquals(userService.getAll(filterUserOptions, page, size, user).size(), allUsers.size());
    }

    @Test
    public void getById_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);

        assertThrows(UnauthorizedOperationException.class, () -> {
            userService.getById(mockUser, 2);
        });
    }

    @Test
    public void getById_ShouldThrowException_WhenUserIsNotAnAdmin() {
        User mockUser = createMockUser();

        assertThrows(UnauthorizedOperationException.class, () -> {
            userService.getById(mockUser, 2);
        });
    }

    @Test
    public void getById_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        User anotherMockUser = createAnotherMockUser();

        when(userRepository.getById(2)).thenReturn(anotherMockUser);

        User result = userService.getById(mockUser, 2);

        Assertions.assertEquals(2, result.getUserId());
    }

    @Test
    public void getUserById_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();

        when(userRepository.getById(1)).thenReturn(mockUser);

        User result = userService.getUserById(1);

        Assertions.assertEquals(1, result.getUserId());
    }

    @Test
    public void getByUsername_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();

        when(userRepository.getByUsername(mockUser.getUsername())).thenReturn(mockUser);

        User result = userService.getByUsername(mockUser.getUsername());

        Assertions.assertEquals(mockUser.getUsername(), result.getUsername());
    }

    @Test
    public void getByEmail_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();

        when(userRepository.getByEmail(mockUser.getEmail())).thenReturn(mockUser);

        User result = userService.getByEmail(mockUser.getEmail());

        Assertions.assertEquals(mockUser.getEmail(), result.getEmail());
    }

    @Test
    public void getByPhoneNumber_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();

        when(userRepository.getByPhoneNumber(mockUser.getPhoneNumber())).thenReturn(mockUser);

        User result = userService.getByPhoneNumber(mockUser.getPhoneNumber());

        Assertions.assertEquals(mockUser.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void getByUsernameOrEmailOrPhone_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();

        when(userRepository.getByUsernameOrEmailOrPhone(mockUser.getPhoneNumber())).thenReturn(mockUser);

        User result = userService.getByUsernameOrEmailOrPhone(mockUser.getPhoneNumber());

        Assertions.assertEquals(mockUser.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void createUser_ShouldThrowException_WhenUsernameExists() {
        User mockUser = createMockUser();
        User anotherMockUser = createMockUser();

        when(userRepository.getByUsername(mockUser.getUsername())).thenReturn(anotherMockUser);

        assertThrows(DuplicateEntityException.class, () -> {
            userService.createUser(mockUser);
        });
    }

    @Test
    public void createUser_ShouldThrowException_WhenEmailExists() {
        User mockUser = createMockUser();
        User anotherMockUser = createMockUser();

        when(userRepository.getByUsername(mockUser.getUsername())).thenThrow(new EntityNotFoundException());
        when(userRepository.getByEmail(mockUser.getEmail())).thenReturn(anotherMockUser);

        assertThrows(DuplicateEntityException.class, () -> {
            userService.createUser(mockUser);
        });
    }

    @Test
    public void createUser_ShouldThrowException_WhenPhoneNumberExists() {
        User mockUser = createMockUser();
        User anotherMockUser = createMockUser();

        when(userRepository.getByUsername(mockUser.getUsername())).thenThrow(new EntityNotFoundException());
        when(userRepository.getByEmail(mockUser.getEmail())).thenThrow(new EntityNotFoundException());
        when(userRepository.getByPhoneNumber(mockUser.getPhoneNumber())).thenReturn(anotherMockUser);

        assertThrows(DuplicateEntityException.class, () -> {
            userService.createUser(mockUser);
        });
    }

    @Test
    public void createUser_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();
        String token = TokenGenerator.generateToken();
        mockUser.setVerificationToken(token);
        mockUser.setAccountVerified(false);

        when(userRepository.getByUsername(mockUser.getUsername())).thenThrow(new EntityNotFoundException());
        when(userRepository.getByEmail(mockUser.getEmail())).thenThrow(new EntityNotFoundException());
        when(userRepository.getByPhoneNumber(mockUser.getPhoneNumber())).thenThrow(new EntityNotFoundException());

        assertDoesNotThrow(() -> {
            emailService.sendVerificationEmail(mockUser.getEmail(), token);
        });

        assertDoesNotThrow(() -> {
            userService.createUser(mockUser);
        });
    }

    @Test
    public void verifyUser_ShouldUpdateUser_WhenValid() {
        User mockUser = createMockUser();
        String token = TokenGenerator.generateToken();
        mockUser.setVerificationToken(token);

        when(userRepository.findByVerificationToken(token)).thenReturn(mockUser);

        assertDoesNotThrow(() -> {
            userService.verifyUser(token);
        });
    }

    @Test
    public void verifyUser_ShouldReturnFalse_WhenUserIsNull() {
        String token = TokenGenerator.generateToken();

        when(userRepository.findByVerificationToken(token)).thenReturn(null);

        assertFalse(userService.verifyUser(token));
    }

    @Test
    public void updateUser_ShouldThrowException_WhenUserIsNotACreatorOrAdmin() {
        User mockUser = createMockUser();
        User anotherMockUser = createAnotherMockUser();

        assertThrows(UnauthorizedOperationException.class, () -> {
            userService.updateUser(anotherMockUser, mockUser);
        });
    }

    @Test
    public void updateUser_ShouldThrowException_WhenUserHasntChangedAnything() {
        User updateUser = createMockUser();
        User modifier = createMockUser();

        when(userRepository.getByEmail(updateUser.getEmail())).thenReturn(modifier);

        assertThrows(InvalidOperationException.class, () -> {
            userService.updateUser(updateUser, modifier);
        });
    }

    @Test
    public void update_ShouldThrowException_WhenUserWithSameEmailExists() {
        User updatedUser = createMockUser();
        User anotherMockUser = createAnotherMockUser();
        User modifier = createMockUser();
        anotherMockUser.setEmail(updatedUser.getEmail());

        when(userRepository.getByEmail(updatedUser.getEmail())).thenReturn(anotherMockUser);
        when(userRepository.getByPhoneNumber(updatedUser.getPhoneNumber())).thenThrow(new EntityNotFoundException());

        assertThrows(DuplicateEntityException.class, () -> {
            userService.updateUser(updatedUser, modifier);
        });
    }

    @Test
    public void update_ShouldThrowException_WhenUserWithSamePhoneNumberExists() {
        User updatedUser = createMockUser();
        User anotherMockUser = createAnotherMockUser();
        User modifier = createMockUser();
        anotherMockUser.setPhoneNumber(updatedUser.getPhoneNumber());

        when(userRepository.getByEmail(updatedUser.getEmail())).thenThrow(new EntityNotFoundException());
        when(userRepository.getByPhoneNumber(updatedUser.getPhoneNumber())).thenReturn(anotherMockUser);

        assertThrows(DuplicateEntityException.class, () -> {
            userService.updateUser(updatedUser, modifier);
        });
    }

    @Test
    public void update_ShouldUpdateUser_WhenValid() {
        User mockUser = createMockUser();
        User updatedUser = createMockUser();
        updatedUser.setPhoneNumber("1111111111");

        when(userRepository.getByEmail(updatedUser.getEmail())).thenThrow(new EntityNotFoundException());
        when(userRepository.getByPhoneNumber(updatedUser.getPhoneNumber())).thenThrow(new EntityNotFoundException());

        assertDoesNotThrow(() -> {
            userService.updateUser(updatedUser, mockUser);
        });
    }

    @Test
    public void deleteUser_ShouldThrowException_WhenUserIsNotCreatorOrAdmin() {
        User mockUser = createMockUser();
        User anotherMockUser = createAnotherMockUser();

        assertThrows(UnauthorizedOperationException.class, () -> {
            userService.deleteUser(anotherMockUser, mockUser.getUserId());
        });
    }

    @Test
    public void deleteUser_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();

        assertDoesNotThrow(() -> {
            userService.deleteUser(mockUser, mockUser.getUserId());
        });
    }

    @Test
    void testMakeAdminThrowsInvalidOperationException() {
        user.setAdmin(true);
        when(userRepository.getById(2)).thenReturn(user);
        assertThrows(InvalidOperationException.class, () -> userService.makeAdmin(admin, 2));
    }

    @Test
    void testMakeAdminSuccess() {
        when(userRepository.getById(2)).thenReturn(user);
        user.setAdmin(false);
        userService.makeAdmin(admin, 2);
        assertTrue(user.isAdmin());
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void testRemoveAdminThrowsInvalidOperationException() {
        user.setAdmin(false);
        when(userRepository.getById(2)).thenReturn(user);
        assertThrows(InvalidOperationException.class, () -> userService.removeAdmin(admin, 2));
    }

    @Test
    void testRemoveAdminSuccess() {
        user.setAdmin(true);
        when(userRepository.getById(2)).thenReturn(user);
        userService.removeAdmin(admin, 2);
        assertFalse(user.isAdmin());
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void testBlockUserThrowsInvalidOperationException() {
        user.setBlocked(true);
        when(userRepository.getById(2)).thenReturn(user);
        assertThrows(InvalidOperationException.class, () -> userService.blockUser(admin, 2));
    }

    @Test
    void testBlockUser() {
        user.setBlocked(false);
        when(userRepository.getById(2)).thenReturn(user);
        userService.blockUser(admin, 2);
        assertTrue(user.isBlocked());
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void testUnblockUserThrowsInvalidOperationException() {
        user.setBlocked(false);
        when(userRepository.getById(2)).thenReturn(user);
        assertThrows(InvalidOperationException.class, () -> userService.unblockUser(admin, 2));
    }

    @Test
    void testUnblockUser() {
        user.setBlocked(true);
        when(userRepository.getById(2)).thenReturn(user);
        userService.unblockUser(admin, 2);
        assertFalse(user.isBlocked());
        verify(userRepository, times(1)).updateUser(user);
    }
}
