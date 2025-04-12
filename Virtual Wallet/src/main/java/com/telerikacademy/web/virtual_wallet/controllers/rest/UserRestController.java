package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.mappers.UserMapper;
import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.models.dtos.UserDTO;
import com.telerikacademy.web.virtual_wallet.models.dtos.UserDTOOut;
import com.telerikacademy.web.virtual_wallet.models.dtos.UserDTOUpdate;
import com.telerikacademy.web.virtual_wallet.services.contracts.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;



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
    public List<UserDTOOut> getAll(@RequestParam(required = false) String username,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String phone,
                                   @RequestParam(required = false) String sortBy,
                                   @RequestParam(required = false) String orderBy,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBySpecific,
                                   @RequestParam(defaultValue = "ASC") String direction) {
        FilterUserOptions filterOptions = new FilterUserOptions(username, phone, email, sortBy, orderBy);

        Page<User> pageOfUsers = userService.getAll(filterOptions, page, size, sortBySpecific, direction);
        return userMapper.toDTOOut(pageOfUsers.getContent());
    }

    @Operation(summary = "Get user by ID", description = "Fetches a user by their unique ID")
    @GetMapping("/{userId}")
    @SecurityRequirement(name = "authHeader")
    public UserDTOOut getById(@RequestHeader HttpHeaders headers, @PathVariable long userId) {
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
    public UserDTOOut create(@RequestBody UserDTO userDto) {
        try {
            User user = userMapper.fromUserDto(userDto);
            userService.create(user);
            return userMapper.toUserDtoOut(userDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Operation(summary = "Updates user by an Id", description = "Updates the desired fields in an User")
    @PutMapping("/{userId}")
    @SecurityRequirement(name = "authHeader")
    public UserDTOOut update(@RequestHeader HttpHeaders headers, @PathVariable long userId,
                             @Valid @RequestBody UserDTOUpdate userDtoUpdate) {
        try {
            User userFromHeader = authorizationHelper.tryGetUser(headers);

            User userFromId = userService.getById(userId);
            User user = userMapper.fromUserDtoUpdateToUser(userDtoUpdate, userFromId);
            userService.update(user, userFromHeader, userId);
            return userMapper.toUserDtoOut(getById2(headers, userId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
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

//    @GetMapping("/verify")
//    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
//        boolean isVerified = userService.verifyEmail(token);
//
//        if(isVerified) {
//            return ResponseEntity.ok("Verified");
//        }
//        return ResponseEntity.badRequest().body("Invalid or expired token.");
//    }
}
