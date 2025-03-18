package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import com.telerikacademy.web.virtual_wallet.services.email_verification.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @Mock
    EmailServiceImpl emailService;

    @InjectMocks
    UserServiceImpl userService;

    private User user1, user2, user4, user5;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("john_doe");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPhoneNumber("1234567890");
        user1.setIsAdmin(true);
        user1.setIsBlocked(false);
        user1.setBalance(100.0);

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane_smith");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPhoneNumber("0987654321");
        user2.setIsAdmin(false);
        user2.setIsBlocked(true);
        user2.setBalance(50.0);

        user4 = new User();
        user4.setId(3L);
        user4.setUsername("jane_smith");
        user4.setFirstName("Jane");
        user4.setLastName("Smith");
        user4.setEmail("jane.smith@example.com");
        user4.setPhoneNumber("0987654321");
        user4.setIsAdmin(false);
        user4.setIsBlocked(true);
        user4.setBalance(50.0);
    }

    @Test
    void getAll_Should_Return_All_Users() {
        // Arrange
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.getAll(any())).thenReturn(users);

        // Act
        List<User> result = userService.getAll(new FilterUserOptions());

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("john_doe", result.get(0).getUsername());
        Assertions.assertEquals("jane_smith", result.get(1).getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).getAll(any());
    }

    @Test
    void getUserCount_Should_Return_All_CountedUsers() {
        // Arrange
        when(userRepository.getUserCount()).thenReturn(2L);

        // Act
        long count = userService.getUserCount();

        // Assert
        Assertions.assertEquals(2, count);
        Mockito.verify(userRepository, Mockito.times(1)).getUserCount();
    }

    @Test
    void getById_Should_Return_User() {
        // Arrange
        when(userRepository.getById(1)).thenReturn(user1);

        // Act
        User result = userService.getById(1L);

        // Assert
        Assertions.assertEquals(user1.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).getById(1L);
    }

    @Test
    void getByUsername_Should_Return_User() {
        // Arrange
        when(userRepository.getByUsername("john_doe")).thenReturn(user1);

        // Act
        User result = userService.getByUsername("john_doe");

        // Assert
        Assertions.assertEquals(user1.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).getByUsername("john_doe");
    }

    @Test
    void getByEmail_Should_Return_User() {
        // Arrange
        when(userRepository.getByEmail("john.doe@example.com")).thenReturn(user1);

        // Act
        User result = userService.getByEmail("john.doe@example.com");

        // Assert
        Assertions.assertEquals(user1.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).getByEmail("john.doe@example.com");
    }

    @Test
    void AlterAdminPermission_Should_Throw_UserNotAdmin() {
        // Arrange, Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.alterAdminPermissions(1, user2, true));
    }

    @Test
    void AlterAdminPermissions_Should_Update_WhenValid() {
        // Arrange
        when(userRepository.getById(user2.getId())).thenReturn(user2);

        // Act
        userService.alterAdminPermissions(user2.getId(), user1, true);

        // Assert
        Assertions.assertTrue(user2.getIsAdmin());
        Mockito.verify(userRepository, Mockito.times(1)).alterAdminPermissions(user2);
    }

    @Test
    void Create_Should_Throw_UserWithUsernameAlreadyExists() {
        // Arrange, Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.create(user4));
    }

    @Test
    void Create_Should_Create_WhenValid() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("testUser");

        Mockito.when(userRepository.getByUsername("testUser"))
                .thenThrow(new EntityNotFoundException("User", "username", newUser.getUsername()));

        // Act
        userService.create(newUser);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).create(newUser);
    }

    @Test
    void Update_Should_Throw_When_UserNotAdminOrCreator() {
        // Arrange, Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.update(user2, user4, user2.getId()));
    }

    @Test
    void Update_Should_Throw_When_UserWithEmailExists() {
        // Arrange
        user2.setEmail("jane.smith@example.com");

        // Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.update(user2, user1, user1.getId()));
    }

    @Test
    void Update_Should_Update_WhenValid() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("test@example.com");

        Mockito.when(userService.getByEmail("test@example.com"))
                .thenThrow(new EntityNotFoundException("User", "email", newUser.getEmail()));

        // Act
        userService.update(newUser, user1, user2.getId());

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).update(newUser, user2.getId());
    }

    @Test
    void delete_Should_Throw_When_UserNotFound() {
        // Arrange
        Mockito.when(userService.getById(user1, 7))
                .thenThrow(new EntityNotFoundException("User", "id", "7"));

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.delete(7, user1));
    }

    @Test
    void delete_Should_Delete_WhenValid() {
        // Arrange
        Mockito.when(userService.getById(user1, user2.getId()))
                .thenReturn(user2);

        // Act
        userService.delete(user2.getId(), user1);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).delete(user2.getId());
    }
}
