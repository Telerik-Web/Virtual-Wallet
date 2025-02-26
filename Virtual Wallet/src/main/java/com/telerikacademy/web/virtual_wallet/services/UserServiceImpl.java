package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.telerikacademy.web.virtual_wallet.helpers.PermissionHelper.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll(FilterUserOptions filterOptions) {
        return userRepository.getAll(filterOptions);
    }

    @Override
    public long getUserCount() {
        return userRepository.getUserCount();
    }

    @Override
    public User getById(User user, int id) {
        checkIfAdmin(user);
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public void alterAdminPermissions(int id, User user, boolean isAdmin) {
        checkIfAdmin(user);

        User userToUpdate = getById(user, id);

        if (isAdmin) {
            userToUpdate.setAdmin(true);
        } else {
            userToUpdate.setAdmin(false);
        }
        userRepository.alterAdminPermissions(userToUpdate);
    }

    @Override
    public void create(User user) {
        boolean exists = true;

        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            exists = false;
        }

        if (exists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }

        userRepository.create(user);
    }

    @Override
    public void update(User user, User userFromHeader, int id) {
        checkIfCreatorOrAdminForUser(userFromHeader, user);

        boolean exists = true;

        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            exists = false;
        }

        if (exists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

        userRepository.update(user, id);
    }

    @Override
    public void delete(int id, User userFromHeader) {
        User user = getById(userFromHeader, id);
        checkIfCreatorOrAdminForUser(userFromHeader, user);
        userRepository.delete(id);
    }
}
