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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/cards")
@Tag(name = "Card Controller", description = "APIs for managing cards")
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

    @Operation(summary = "Return all cards", description = "Returns all created cards if user is an admin")
    @GetMapping
    @SecurityRequirement(name = "authHeader")
    public List<CardDTOOut> getAllCards(@RequestHeader HttpHeaders headers) {
        try{
            User user = authenticationHelper.tryGetUser(headers);
            List<Card> cardList = cardService.getAllCards(user);
            return cardMapper.toDTOOut(cardList);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @Operation(summary = "Return a card by ID", description = "Returns a card matching an ID")
    @GetMapping("/{cardId}")
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Return all user's cards", description = "Get a single user by ID and then return his cards")
    @GetMapping("/users/{userId}")
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Create a card", description = "Creates a card for logged in user")
    @PostMapping
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Update a card", description = "Updates a card for logged in user")
    @PutMapping("/{cardId}")
    @SecurityRequirement(name = "authHeader")
    public void update(@Valid @RequestBody CardDTO cardDTO , @PathVariable int cardId, @RequestHeader HttpHeaders headers) {
        try{
            User user = authenticationHelper.tryGetUser(headers);
            Card card = cardMapper.fromDto(cardId, cardDTO, user);
            cardService.update(card, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Delete a card", description = "Deletes a card for logged in user")
    @DeleteMapping("/{cardId}")
    @SecurityRequirement(name = "authHeader")
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
