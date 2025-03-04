package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.mappers.UserMapper;
import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;


//get each card by id and all cards

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "APIs for managing users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authorizationHelper;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authorizationHelper = authenticationHelper;
    }

    @Operation(summary = "Returns all users", description = "Returns all users with their proper fields.")
    @GetMapping
    public List<UserDtoOut> getAll(@RequestParam(required = false) String username,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String phone,
                                   @RequestParam(required = false) String sortBy,
                                   @RequestParam(required = false) String orderBy) {
        FilterUserOptions filterOptions = new FilterUserOptions(username, phone, email, sortBy, orderBy);
        return userMapper.toDTOOut(userService.getAll(filterOptions));
    }

    @Operation(summary = "Get user by ID", description = "Fetches a user by their unique ID")
    @GetMapping("/{userId}")
    @SecurityRequirement(name = "authHeader")
    public UserDtoOut getById(@RequestHeader HttpHeaders headers, @PathVariable long userId) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            User userToReturn = userService.getById(user, userId);
            return userMapper.toUserDtoOut(userToReturn);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public User getById2(@RequestHeader HttpHeaders headers, @PathVariable long userId) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            User userToReturn = userService.getById(user, userId);
            return userService.getById(user, userId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Create a User", description = "Create a user with unique all its fields.")
    @PostMapping
    public UserDtoOut create(@RequestBody UserDTO userDto) {
        try {
            User user = userMapper.fromUserDto(userDto);
            userService.create(user);
            return userMapper.toUserDtoOut(userDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
//        {
//            "firstName": "Johgn",
//                "lastName": "Doe",
//                "username": "newUser",
//                "email": "newuser@example.com",
//                "phone": "123456789",
//                "password": "securePassword"
//        }
    }

    @Operation(summary = "Updates user by an Id", description = "Updates the desired fields in an User")
    @PutMapping("/{userId}")
    @SecurityRequirement(name = "authHeader")
    public UserDtoOut update(@RequestHeader HttpHeaders headers, @PathVariable long userId,
                             @RequestBody UserDTOUpdate userDtoUpdate) {
        try {
            User userFromHeader = authorizationHelper.tryGetUser(headers);
            User user = userMapper.fromUserDtoUpdateToUser(userDtoUpdate, userId);
            userService.update(user, userFromHeader, userId);
            return userMapper.toUserDtoOut(getById2(headers, userId));
            //return userDto;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
//        {
//            "firstName": "Ivan",
//                "lastName": "Doe",
//                "email": "newuser@example.com",
//                "phone": "123456789",
//                "password": "securePassword"
//        }
    }

    @Operation(summary = "Alter admin permissions", description = "Changes user permissions " +
            "if isAdmin is true, promotes the user to admin, else removes admin permissions")
    @PatchMapping("/{userId}/admin")
    @SecurityRequirement(name = "authHeader")
    public void alterAdminPermissions(@PathVariable long userId, @RequestHeader HttpHeaders headers,
                                      @RequestParam boolean isAdmin) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            userService.alterAdminPermissions(userId, user, isAdmin);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Deletes an user by Id", description = "Deletes an user by their unique ID")
    @DeleteMapping("/{userId}")
    @SecurityRequirement(name = "authHeader")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long userId) {
        try {
            User userFromHeader = authorizationHelper.tryGetUser(headers);
            userService.delete(userId, userFromHeader);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
