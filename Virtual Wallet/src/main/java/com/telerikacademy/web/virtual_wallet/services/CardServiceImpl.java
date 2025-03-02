package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.CardRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Card getById(long id) {
        return cardRepository.findById(id);
    }

    @Override
    public Set<Card> getCardsByUserId(long userId) {
        User user = userRepository.getById(userId);
        return cardRepository.findByUser(user);
    }

    @Override
    public void create(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void update(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }
}
