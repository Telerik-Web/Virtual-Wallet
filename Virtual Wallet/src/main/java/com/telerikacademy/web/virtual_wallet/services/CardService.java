package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;

import java.util.List;
import java.util.Set;

public interface CardService {

    List<Card> getAllCards(User user);

    Card getById(long id, User user);

    List<Card> getCardsByUserId(long userId, User user);

    void create(Card card, User user);

    void update(Card card, User user);

    void delete(Card card, User user);
}
