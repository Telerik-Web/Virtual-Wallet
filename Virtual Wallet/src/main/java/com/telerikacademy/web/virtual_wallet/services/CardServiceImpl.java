package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.CardRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.telerikacademy.web.virtual_wallet.helpers.PermissionHelper.checkIfCreatorOrAdminForUser;

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
        Card card = cardRepository.findById(id);
        if(card == null){
            throw new EntityNotFoundException("Card", id);
        }
        return card;
    }

    @Override
    public Set<Card> getCardsByUserId(long userId, User userFromHeader) {
        User user = userRepository.getById(userId);

        checkIfCreatorOrAdminForUser(user, userFromHeader);

        return cardRepository.findByUser(user);
    }

    @Override
    public void create(Card card, User userFromHeader) {

        boolean exists = userFromHeader.getCards().stream()
                    .anyMatch(existingCard -> existingCard.getId() == card.getId());

        if(exists){
            throw new DuplicateEntityException("Card", "user", userFromHeader.getUsername());
        }
        else{
            card.setUser(userFromHeader);
            cardRepository.save(card);
        }

    }

    @Override
    public void update(Card card, User userFromHeader) {
        checkIfCreatorOrAdminForUser(userFromHeader, card.getUser());
        cardRepository.save(card);
    }

    @Override
    public void delete(Card card, User userFromHeader) {
        checkIfCreatorOrAdminForUser(userFromHeader, card.getUser());
        cardRepository.delete(card);
    }
}
