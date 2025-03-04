package com.telerikacademy.web.virtual_wallet.mappers;


import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.CardDTO;
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

    public Card fromDTO(CardDTO cardDTO) {
        Card card = new Card();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setExpirationDate(cardDTO.getExpirationDate());
        card.setCheckNumber(cardDTO.getCheckNumber());

        return card;
    }
}
