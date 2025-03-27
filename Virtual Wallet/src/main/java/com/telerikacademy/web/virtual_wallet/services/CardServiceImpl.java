package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.CardRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.telerikacademy.web.virtual_wallet.helpers.PermissionHelper.*;

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
    public List<Card> getAllCards(User user) {
        checkIfAdmin(user);
        return cardRepository.findAll();
    }

    @Override
    public Card getById(long id, User user) {

        Card card = cardRepository.findById(id);

        if(card == null){
            throw new EntityNotFoundException("Card", id);
        }

        checkIfCreatorOrAdminForUser(user, card.getUser());
        return card;
    }

    @Override
    public List<Card> getCardsByUserId(long userId, User userFromHeader) {
        User user = userRepository.getById(userId);

        checkIfCreatorOrAdminForUser(userFromHeader, user);

        return cardRepository.findByUser(user);
    }

    @Override
    public void create(Card card, User userFromHeader) {

        if (cardRepository.existsByUserAndNumber(userFromHeader, card.getCardNumber())) {
            throw new DuplicateEntityException("Card", "user", userFromHeader.getUsername());
        }

        card.setUser(userFromHeader);
        cardRepository.save(card);

    }

    @Override
    public void update(Card card, User userFromHeader) {
        checkIfCreatorOrAdminForCard(userFromHeader, card);
        cardRepository.save(card);
    }

    @Override
    public void delete(Card card, User userFromHeader) {
        checkIfCreatorOrAdminForCard(userFromHeader, card);
        cardRepository.delete(card);
    }
}
