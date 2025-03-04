package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;

import java.util.Set;

public interface CardService {

    Card getById(long id);

    Set<Card> getCardsByUserId(long userId, User user);

    void create(Card card, User user);

    void update(Card card, User user);

    void delete(Card card, User user);
}
