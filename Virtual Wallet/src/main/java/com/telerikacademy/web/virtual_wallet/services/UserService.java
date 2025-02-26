package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    List<User> getAll(FilterUserOptions filterOptions);

    long getUserCount();

    User getById(User user, int id);

    User getByUsername(String username);

    User getByEmail(String email);

    void alterAdminPermissions(int id, User user, boolean isAdmin);

    void create(User user);

    void update(User user, User userFromHeader, int id);

    void delete(int id, User userFromHeader);
}
