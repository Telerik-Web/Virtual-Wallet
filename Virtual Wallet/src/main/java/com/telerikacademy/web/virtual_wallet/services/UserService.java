package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.CardDTO;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAll(FilterUserOptions filterOptions);

    long getUserCount();

    User getById(int id);

    User getById(User user, int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    void alterAdminPermissions(int id, User user, boolean isAdmin);

    void create(User user);

    void update(User user, User userFromHeader, int id);

    void delete(int id, User userFromHeader);

    @Transactional
    public void addCardToUser(int userId, Card card);

    @Transactional
    public Set<CardDTO> getAllCardsForUser(int userId);
}
