package com.telerikacademy.web.virtual_wallet.mappers;


import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.CardDTO;
import com.telerikacademy.web.virtual_wallet.repositories.CardRepositoryImpl;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardDTO cardToDTO(Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardNumber(String.valueOf(card.getCardNumber()));
        cardDTO.setExpirationDate(card.getExpirationDate());
        cardDTO.setCheckNumber(String.valueOf(card.getCheckNumber()));
        return cardDTO;
    }


}
