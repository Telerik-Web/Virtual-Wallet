package com.telerikacademy.web.virtual_wallet.mappers;


import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.models.dtos.CardDTO;
import com.telerikacademy.web.virtual_wallet.models.dtos.CardDTOExpiryDate;
import com.telerikacademy.web.virtual_wallet.models.dtos.CardDTOOut;
import com.telerikacademy.web.virtual_wallet.services.contracts.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CardMapper {

    private final CardService cardService;

    @Autowired
    public CardMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public CardDTO cardToDTO(Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardNumber(String.valueOf(card.getCardNumber()));
        cardDTO.setCheckNumber(String.valueOf(card.getCheckNumber()));
        return cardDTO;
    }

    public Card fromDTO(CardDTO cardDTO) {
        Card card = new Card();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCheckNumber(cardDTO.getCheckNumber());

        return card;
    }

    public Card fromDTO2(CardDTOExpiryDate cardDTO) {
        Card card = new Card();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCheckNumber(cardDTO.getCheckNumber());
        card.setExpirationDate(cardDTO.getExpirationDate());

        return card;
    }

    public Card fromDto(int id, CardDTO dto, User user) {
        Card card = cardService.getById(id, user);
        card.setCardNumber(dto.getCardNumber());
        card.setCheckNumber(dto.getCheckNumber());

        return card;
    }

    public CardDTOOut cardDTOToOut(Card card) {
        CardDTOOut cardDTOOut = new CardDTOOut();
        cardDTOOut.setId(card.getId());
        cardDTOOut.setCardNumber(card.getCardNumber());
        cardDTOOut.setCheckNumber(card.getCheckNumber());
        cardDTOOut.setExpirationDate(card.getExpirationDate());
        cardDTOOut.setCardHolder(card.getCardHolder());

        return cardDTOOut;
    }

    public List<CardDTOOut> toDTOOut(List<Card> cardList){
        List<CardDTOOut> cardDTOS = new ArrayList<>();
        for (Card card : cardList){
            CardDTOOut cardDTOOut = new CardDTOOut();
            cardDTOOut.setId(card.getId());
            cardDTOOut.setCardNumber(card.getCardNumber());
            cardDTOOut.setCheckNumber(card.getCheckNumber());
            cardDTOOut.setExpirationDate(card.getExpirationDate());
            cardDTOOut.setCardHolder(card.getCardHolder());

            cardDTOS.add(cardDTOOut);
        }

        return cardDTOS;
    }
}
