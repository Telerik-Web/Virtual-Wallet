package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.telerikacademy.web.virtual_wallet.Helpers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
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
        List<User> listUsers = Arrays.asList(user1, user2);
        Page<User> users = new PageImpl<>(listUsers);
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(users);

        // Act
        Page<User> result = userService.getAll(createMockFilterUserOptions(), 1, 10, "username", "desc");

        // Assert
        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("john_doe", result.getContent().get(0).getUsername());
        Assertions.assertEquals("jane_smith", result.getContent().get(1).getUsername());
    }

    @Test
    void getUserCount_Should_Return_All_CountedUsers() {
        // Arrange
        when(userRepository.count()).thenReturn(2L);

        // Act
        long count = userService.getUserCount();

        // Assert
        Assertions.assertEquals(2, count);
        Mockito.verify(userRepository, Mockito.times(1)).count();
    }

    @Test
    void getById_Should_Return_User() {
        // Arrange
        when(userRepository.findUserById(anyLong())).thenReturn(user1);

        // Act
        User result = userService.getById(1L);

        // Assert
        Assertions.assertEquals(user1.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findUserById(anyLong());
    }

    @Test
    void getByUsername_Should_Return_User() {
        // Arrange
        when(userRepository.findByUsername("john_doe")).thenReturn(user1);

        // Act
        User result = userService.getByUsername("john_doe");

        // Assert
        Assertions.assertEquals(user1.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername("john_doe");
    }

    @Test
    void getByEmail_Should_Return_User() {
        // Arrange
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(user1);

        // Act
        User result = userService.getByEmail("john.doe@example.com");

        // Assert
        Assertions.assertEquals(user1.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void getByPhoneNumber_Should_Return_User() {
        // Arrange
        when(userRepository.findByPhoneNumber("0888")).thenReturn(user1);

        // Act
        User result = userService.getByPhoneNumber("0888");

        // Assert
        Assertions.assertEquals(user1.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findByPhoneNumber("0888");
    }

    @Test
    void AlterAdminPermission_Should_Throw_UserNotAdmin() {
        // Arrange, Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.alterAdminPermissions(1, user2, true));
    }

    @Test
    void AlterAdminPermissions_Should_Admin_WhenValid() {
        // Arrange
        when(userRepository.findUserById(anyLong())).thenReturn(user2);

        // Act
        userService.alterAdminPermissions(user2.getId(), user1, true);

        // Assert
        Assertions.assertTrue(user2.getIsAdmin());
        Mockito.verify(userRepository, Mockito.times(1)).save(user2);
    }

    @Test
    void AlterAdminPermissions_Should_NotAdmin_WhenValid() {
        // Arrange
        when(userRepository.findUserById(anyLong())).thenReturn(user2);
        user2.setIsAdmin(true);

        // Act
        userService.alterAdminPermissions(user2.getId(), user1, false);

        // Assert
        Assertions.assertFalse(user2.getIsAdmin());
        Mockito.verify(userRepository, Mockito.times(1)).save(user2);
    }

    @Test
    void AlterBlockPermission_Should_Throw_UserNotBlocked() {
        // Arrange, Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.alterBlockPermissions(1, user2, true));
    }

    @Test
    void AlterBlockPermissions_Should_Block_WhenValid() {
        // Arrange
        when(userRepository.findUserById(anyLong())).thenReturn(user2);

        // Act
        userService.alterBlockPermissions(user2.getId(), user1, true);

        // Assert
        Assertions.assertTrue(user2.getIsBlocked());
        Mockito.verify(userRepository, Mockito.times(1)).save(user2);
    }

    @Test
    void AlterBlockPermissions_Should_Unblock_WhenValid() {
        // Arrange
        when(userRepository.findUserById(anyLong())).thenReturn(user2);
        user2.setIsBlocked(true);

        // Act
        userService.alterBlockPermissions(user2.getId(), user1, false);

        // Assert
        Assertions.assertFalse(user2.getIsBlocked());
        Mockito.verify(userRepository, Mockito.times(1)).save(user2);
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

        Mockito.when(userRepository.findByUsername("testUser"))
                .thenThrow(new EntityNotFoundException("User", "username", newUser.getUsername()));

        // Act
        userService.create(newUser);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).save(newUser);
    }

    @Test
    void verifyUser_Should_Return_False_WhenUserNotFound() {
        // Arrange
        String token = "testUser";
        Mockito.when(userRepository.findByVerificationToken(token)).thenReturn(null);

        // Act
        boolean result = userService.verifyUser(token);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void verifyUser_Should_Return_True_WhenUserFound() {
        // Arrange
        String token = "testUser";
        Mockito.when(userRepository.findByVerificationToken(token)).thenReturn(user2);

        // Act
        boolean result = userService.verifyUser(token);

        // Assert
        Assertions.assertTrue(result);
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
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("test@example.com");
        existingUser.setUsername("john_doe");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("test@example.com");
        updatedUser.setUsername("updated_john_doe");

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);
        Mockito.when(userService.getByEmail("test@example.com")).thenReturn(existingUser);

        userService.update(updatedUser, existingUser, existingUser.getId());

        Mockito.verify(userRepository, Mockito.times(1)).save(updatedUser);
    }

    @Test
    void delete_Should_Throw_When_UserNotFound() {
        // Arrange
        Mockito.when(userRepository.findUserById(anyLong())).thenReturn(user1);

        Mockito.when(userService.getById(anyLong())).thenThrow(new EntityNotFoundException("User", "username", user1.getUsername()));

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.delete(anyLong(), user1));
    }

    @Test
    void delete_Should_Delete_WhenValid() {
        // Arrange
        User adminUser = createMockAdminUser();
        User user = createMockUser();
        when(userRepository.findUserById(anyLong())).thenReturn(user);

        Mockito.when(userService.getById(adminUser, anyLong()))
                .thenReturn(user);

        // Act
        userService.delete(adminUser.getId(), user);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }
}
