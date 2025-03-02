package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.Card;

import java.util.Set;

public interface CardService {

    Card getById(long id);

    Set<Card> getCardsByUserId(long userId);

    void create(Card card);

    void update(Card card);

    void delete(Card card);
}
