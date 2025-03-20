package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.CardDTO;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface UserService {
    Page<User> getAll(FilterUserOptions filterOptions, int page, int size, String sortBy, String direction);

    long getUserCount();

    User getById(long id);

    User getById(User user, long id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    void alterAdminPermissions(long id, User user, boolean isAdmin);

    void alterBlockPermissions(long id, User user, boolean isBlocked);

    void create(User user);

    void update(User user, User userFromHeader, long id);

    void delete(long id, User userFromHeader);

    boolean verifyUser(String token);
}
