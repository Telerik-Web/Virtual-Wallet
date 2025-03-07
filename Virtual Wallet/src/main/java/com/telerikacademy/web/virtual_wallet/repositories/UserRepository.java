package com.telerikacademy.web.virtual_wallet.repositories;


import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getAll(FilterUserOptions filterOptions);

    long getUserCount();

    User getById(long id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    void alterAdminPermissions(User user);

    void alterBlock(User user);

    void create(@Valid User user);

    void update(@Valid User user, long id);

    void delete(long id);

    //User findByVerificationToken(String token);
}
