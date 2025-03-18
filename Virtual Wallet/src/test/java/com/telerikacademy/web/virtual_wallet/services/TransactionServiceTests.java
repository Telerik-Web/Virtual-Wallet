package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.TransactionRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.web.virtual_wallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    UserRepositoryImpl userRepository;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionServiceImpl transactionService;

    @Test
    void transferFunds_Should_Throw_WhenSenderIsNull() {
        // Arrange
        User recipient = createMockAdminUser();
        double amount = 1.1;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferFunds(null, recipient, amount));
    }

    @Test
    void transferFunds_Should_Throw_WhenRecipientIsNull() {
        // Arrange
        User sender = createMockAdminUser();
        double amount = 1.1;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferFunds(sender, null, amount));
    }

    @Test
    void transferFunds_Should_Throw_WhenAmountIsZero() {
        // Arrange
        User sender = createMockAdminUser();
        User recipient = createMockBlockedUser();
        double amount = 0.0;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferFunds(sender, recipient, amount));
    }

    @Test
    void transferFunds_Should_Throw_WhenInsufficientFunds() {
        // Arrange
        User sender = createMockAdminUser();
        sender.setBalance(0);
        User recipient = createMockBlockedUser();
        double amount = 10.0;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferFunds(sender, recipient, amount));
    }

    @Test
    void transferFunds_Should_UpdateTransfer_When_Valid() {
        // Arrange
        User sender = createMockAdminUser();
        User recipient = createMockBlockedUser();
        double amount = 1.0;

        // Act
        transactionService.transferFunds(sender, recipient, amount);

        // Assert
        Assertions.assertEquals(99, sender.getBalance());
        Assertions.assertEquals(51, recipient.getBalance());

        Mockito.verify(userRepository, Mockito.times(1)).update(sender, sender.getId());
        Mockito.verify(userRepository, Mockito.times(1)).update(recipient, recipient.getId());
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void getAllTransactionsForUser_Should_Return_All_Transactions() {
        // Arrange
        User user = createMockAdminUser();

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> transactionService.getAllTransactionsForUser(user.getId()));

        // Verify interactions
        Mockito.verify(transactionRepository, Mockito.times(1)).findAllByUserId(user.getId());
    }

    @Test
    void filterTransactions_Should_Return_All_Transactions() {
        // Arrange
        User user = createMockAdminUser();

        // Act & Assert
        Assertions.assertDoesNotThrow(() ->
                transactionService.filterTransactions(null, null, null, null, user));

        // Verify repository call
        Mockito.verify(transactionRepository, Mockito.times(1)).findAllByUserId(user.getId());
    }

    @Test
    void filterTransactions_Should_Return_All_IncomingTransactions() {
        // Arrange
        User user = createMockAdminUser();
        List<Transaction> transactions = new ArrayList<>();
        Transaction incoming = new Transaction();
        incoming.setRecipient(user);
        transactions.add(incoming);

        // Act & Assert
        Assertions.assertDoesNotThrow(() ->
                transactionService.filterTransactions(null, null, null, true, user));

        // Verify repository call
        Mockito.verify(transactionRepository, Mockito.times(1)).findAllByUserId(user.getId());
    }

    @Test
    void filterTransactions_Should_Return_All_OutgoingTransactions() {
        // Arrange
        User user = createMockAdminUser();
        List<Transaction> transactions = new ArrayList<>();
        Transaction outgoing = new Transaction();
        outgoing.setSender(user);
        transactions.add(outgoing);

        // Act & Assert
        Assertions.assertDoesNotThrow(() ->
                transactionService.filterTransactions(null, null, null, false, user));

        // Verify repository call
        Mockito.verify(transactionRepository, Mockito.times(1)).findAllByUserId(user.getId());
    }

    @Test
    void sortTransactions_ByAmount_Ascending() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        transactions.add(createMockTransaction2());

        // Act
        Page<Transaction> sorted = transactionService.
                sortTransactionsWithPagination(transactions, "amount", true, 0, transactions.size());

        // Assert
        Assertions.assertEquals(100, sorted.getContent().get(0).getAmount());
        Assertions.assertEquals(200, sorted.getContent().get(1).getAmount());
    }

    @Test
    void sortTransactions_ByAmount_Descending() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        transactions.add(createMockTransaction2());

        // Act
        Page<Transaction> sorted = transactionService.
                sortTransactionsWithPagination(transactions, "amount", false, 0, transactions.size());

        // Assert
        Assertions.assertEquals(200, sorted.getContent().get(0).getAmount());
        Assertions.assertEquals(100, sorted.getContent().get(1).getAmount());
    }

    @Test
    void sortTransactions_ByDate_Ascending() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        transactions.add(createMockTransaction2());

        // Act
        Page<Transaction> sorted = transactionService.
                sortTransactionsWithPagination(transactions, "date", true, 0, transactions.size());

        // Assert
        Assertions.assertEquals(LocalDateTime.of(2025, 3, 9, 17, 26), sorted.getContent().get(0).getCreatedAt());
        Assertions.assertEquals(LocalDateTime.of(2025, 4, 9, 17, 26), sorted.getContent().get(1).getCreatedAt());
    }
}
