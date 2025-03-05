package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.mappers.CardMapper;
import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.CardDTO;
import com.telerikacademy.web.virtual_wallet.models.CardDTOOut;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/cards")
public class CardRestController {

    private final CardService cardService;
    private final CardMapper cardMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CardRestController(CardService cardService, CardMapper cardMapper, AuthenticationHelper authenticationHelper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<CardDTOOut> getAllCards(@RequestHeader HttpHeaders headers) {
        try{
            User user = authenticationHelper.tryGetUser(headers);
            List<Card> cardList = cardService.getAllCards(user);
            return cardMapper.toDTOOut(cardList);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @GetMapping("/{cardId}")
    public CardDTOOut getCardById(@PathVariable int cardId, @RequestHeader HttpHeaders headers) {
        try{
            User user = authenticationHelper.tryGetUser(headers);
            Card card = cardService.getById(cardId, user);
            return cardMapper.cardDTOToOut(card);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/users/{userId}")
    public List<CardDTOOut> getUsersCards(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        try{
            User user = authenticationHelper.tryGetUser(headers);
            List<Card> cardList = cardService.getCardsByUserId(userId, user);
            return cardMapper.toDTOOut(cardList);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void create(@Valid @RequestBody CardDTO cardDTO, @RequestHeader HttpHeaders headers) {
        try{
            User user = authenticationHelper.tryGetUser(headers);
            Card card = cardMapper.fromDTO(cardDTO);
            cardService.create(card, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{cardId}")
    public void update(@Valid @RequestBody CardDTO cardDTO , @PathVariable int cardId, @RequestHeader HttpHeaders headers) {
        try{
            User user = authenticationHelper.tryGetUser(headers);
            Card card = cardMapper.fromDto(cardId, cardDTO, user);
            cardService.update(card, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{cardId}")
    public void delete(@PathVariable int cardId, @RequestHeader HttpHeaders headers) {
        try{
            User user = authenticationHelper.tryGetUser(headers);
            Card card = cardService.getById(cardId, user);
            cardService.delete(card, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
