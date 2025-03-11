package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
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
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.getAll(any())).thenReturn(users);

        List<User> users2 = userService.getAll(new FilterUserOptions());

        Assertions.assertEquals(2, users2.size());
        Assertions.assertEquals("john_doe", users2.get(0).getUsername());
        Assertions.assertEquals("jane_smith", users2.get(1).getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getAll(any());
    }

    @Test
    void getUserCount_Should_Return_All_CountedUsers() {
        when(userRepository.getUserCount()).thenReturn(2L);

        long count = userService.getUserCount();

        Assertions.assertEquals(2, count);

        Mockito.verify(userRepository, Mockito.times(1)).getUserCount();
    }

    @Test
    void getById_Should_Return_User() {
        when(userRepository.getById(1)).thenReturn(user1);

        User user3 = userService.getById(1L);

        Assertions.assertEquals(user1.getUsername(), user3.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getById(1L);
    }

    @Test
    void getByIdWithUser_Should_Return_User() {
        when(userRepository.getById(1L)).thenReturn(user1);

        User user3 = userService.getById(user1, 1L);

        Assertions.assertEquals(user1.getUsername(), user3.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getById(1L);
    }

    @Test
    void getByUsername_Should_Return_User() {
        when(userRepository.getByUsername("john_doe")).thenReturn(user1);

        User user3 = userService.getByUsername("john_doe");

        Assertions.assertEquals(user1.getUsername(), user3.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getByUsername("john_doe");
    }

    @Test
    void getByEmail_Should_Return_User() {
        when(userRepository.getByEmail("john.doe@example.com")).thenReturn(user1);

        User user3 = userService.getByEmail("john.doe@example.com");

        Assertions.assertEquals(user1.getUsername(), user3.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getByEmail("john.doe@example.com");
    }

    @Test
    void getByPhone_Should_Return_User() {
        when(userRepository.getByPhoneNumber("1234567890")).thenReturn(user1);

        User user3 = userService.getByPhoneNumber("1234567890");

        Assertions.assertEquals(user1.getUsername(), user3.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getByPhoneNumber("1234567890");
    }

    @Test
    void AlterAdminPermission_Should_Throw_UserNotAdmin() {
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.alterAdminPermissions(1, user2, true));
    }

    @Test
    void AlterAdminPermissions_Should_Update_WhenValid() {
        when(userRepository.getById(user2.getId())).thenReturn(user2);
        userService.alterAdminPermissions(user2.getId(), user1, true);
        Assertions.assertEquals(true, user2.getIsAdmin());
        Mockito.verify(userRepository, Mockito.times(1)).alterAdminPermissions(user2);
    }

    @Test
    void Create_Should_Throw_UserWithUsernameAlreadyExists() {
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.create(user4));
    }

    @Test
    void Create_Should_Create_WhenValid() {
        User newUser = new User();
        newUser.setUsername("testUser");
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        newUser.setEmail("test@example.com");
        newUser.setPhoneNumber("1234567890");
        newUser.setIsAdmin(false);
        newUser.setIsBlocked(false);
        newUser.setBalance(0.0);

        Mockito.when(userRepository.getByUsername("testUser"))
                .thenThrow(new EntityNotFoundException("User", "username", newUser.getUsername()));

        userService.create(newUser);

        Mockito.verify(userRepository, Mockito.times(1)).create(newUser);
    }

    @Test
    void Update_Should_Throw_When_UserNotAdminOrCreator() {
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.update(user2, user4, user2.getId()));
    }

    @Test
    void Update_Should_Throw_When_UserWithEmailExists() {
        user2.setEmail("jane.smith@example.com");
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.update(user2, user1, user1.getId()));
    }

    @Test
    void Update_Should_Update_WhenValid() {
        User newUser = new User();
        newUser.setUsername("testUser");
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        newUser.setEmail("test@example.com");
        newUser.setPhoneNumber("1234567890");
        newUser.setIsAdmin(false);
        newUser.setIsBlocked(false);
        newUser.setBalance(0.0);

        Mockito.when(userService.getByEmail("test@example.com"))
                .thenThrow(new EntityNotFoundException("User", "email", newUser.getEmail()));

        userService.update(newUser, user1, user2.getId());
        Mockito.verify(userRepository, Mockito.times(1)).update(newUser, user2.getId());
    }

    @Test
    void delete_Should_Throw_When_UserNotFound() {

        Mockito.when(userService.getById(user1, 7))
                .thenThrow(new EntityNotFoundException("User", "id", "7"));

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.delete(7, user1));
    }

    @Test
    void delete_Should_Throw_When_UserNotAdminOrCreator() {
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.delete(user2.getId(), user4));
    }

    @Test
    void delete_Should_Delete_WhenValid() {
        Mockito.when(userService.getById(user1, user2.getId()))
                .thenReturn(user2);

        userService.delete(user2.getId(), user1);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user2.getId());
    }
}
