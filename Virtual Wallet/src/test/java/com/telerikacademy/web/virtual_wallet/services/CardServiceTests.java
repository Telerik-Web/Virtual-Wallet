package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.CardRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.telerikacademy.web.virtual_wallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTests {

    @Mock
    CardRepository cardRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CardServiceImpl cardService;


    @Test
    void getAllCards_Should_CallRepository_When_UserIsAnAdmin() {
        // Arrange
        User admin = createMockAdminUser();
        List<Card> cards = createMockCardsList();
        Mockito.when(cardRepository.findAll()).thenReturn(cards);

        // Act
        cardService.getAllCards(admin);

        // Assert
        Mockito.verify(cardRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getAllCards_Should_Throw_When_UserIsNotAnAdmin() {
        // Arrange
        User user = createMockUser();

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> cardService.getAllCards(user));
    }

    @Test
    void getById_Should_CallRepository_When_UserIsAnAdmin() {
        // Arrange
        User admin = createMockAdminUser();
        Card card = createMockCard();
        Mockito.when(cardRepository.findById(Mockito.anyLong()))
                .thenReturn(card);

        // Act
        cardService.getById(Mockito.anyLong(), admin);

        // Assert
        Mockito.verify(cardRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void getById_Should_Throw_When_IdIsNull() {
        // Arrange
        User user = createMockAdminUser();
        Mockito.when(cardRepository.findById(Mockito.anyLong()))
                .thenReturn(null);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> cardService.getById(Mockito.anyLong(), user));
    }

    @Test
    void getById_Should_Throw_When_UserNotCreatorOrAdmin() {
        // Arrange
        User user = createMockUser();
        Card card = createMockCard();
        Mockito.when(cardRepository.findById(Mockito.anyLong()))
                .thenReturn(card);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> cardService.getById(Mockito.anyLong(), user));
    }

    @Test
    void getCardsByUserId_Should_CallRepository_When_UserIsAnAdmin() {
        // Arrange
        User admin = createMockAdminUser();
        Mockito.when(userRepository.getById(Mockito.anyLong()))
                .thenReturn(admin);

        // Act
        cardService.getCardsByUserId(Mockito.anyLong(), admin);

        // Assert
        Mockito.verify(cardRepository, Mockito.times(1)).findByUser(admin);
    }

    @Test
    void getCardsByUserId_Should_Throw_When_UserNotCreatorOrAdmin() {
        // Arrange
        User user = createMockUser();
        User nonCreator = createMockUser();
        nonCreator.setId(2L);
        Mockito.when(userRepository.getById(Mockito.anyLong()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> cardService.getCardsByUserId(Mockito.anyLong(), nonCreator));
    }

    @Test
    void create_Should_CallRepository_When_CardDoesNotExist() {
        // Arrange
        Card card = createMockCard();
        User user = createMockUser();

        // Act
        cardService.create(card, user);

        // Assert
        Mockito.verify(cardRepository, Mockito.times(1)).save(card);
    }

    @Test
    void create_Should_Throw_When_CardAlreadyExists() {
        // Arrange
        Mockito.when(cardRepository.existsByUserAndNumber(Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);

        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> cardService.create(createMockCard(), createMockUser()));
    }

    @Test
    void update_Should_CallRepository_When_UserIsACreatorOrAdmin() {
        // Arrange
        User admin = createMockAdminUser();
        Card card = createMockCard();

        // Act
        cardService.update(card, admin);

        // Assert
        Mockito.verify(cardRepository, Mockito.times(1)).save(card);
    }

    @Test
    void update_Should_Throw_When_UserNotCreatorOrAdmin() {
        // Arrange
        User user = createMockUser();
        Card card = createMockCard();

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> cardService.update(card, user));
    }

    @Test
    void delete_Should_CallRepository_When_UserIsACreatorOrAdmin() {
        // Arrange
        User admin = createMockAdminUser();
        Card card = createMockCard();

        // Act
        cardService.delete(card, admin);

        // Assert
        Mockito.verify(cardRepository, Mockito.times(1)).delete(card);
    }

    @Test
    void delete_Should_Throw_When_UserNotCreatorOrAdmin() {
        // Arrange
        User user = createMockUser();
        Card card = createMockCard();

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> cardService.delete(card, user));
    }
}
