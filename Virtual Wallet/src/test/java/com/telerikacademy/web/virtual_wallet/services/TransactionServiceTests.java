package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.TransactionRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepositoryImpl;
import com.telerikacademy.web.virtual_wallet.services.email_verification.LargeTransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.web.virtual_wallet.Helpers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    UserRepositoryImpl userRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    LargeTransactionService largeTransactionService;

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

        verify(userRepository, times(1)).update(sender, sender.getId());
        verify(userRepository, times(1)).update(recipient, recipient.getId());
        verify(transactionRepository, times(1)).save(Mockito.any());
    }

    @Test
    void transferFunds_Should_SetTransactionToPending_WhenAmountExceedsThreshold() {
        // Arrange
        User sender = createMockAdminUser();
        sender.setBalance(20000);
        User recipient = createMockUser();
        double amount = 15000.0;

        Transaction mockTransaction = new Transaction();
        mockTransaction.setAmount(amount);
        mockTransaction.setSender(sender);
        mockTransaction.setRecipient(recipient);
        mockTransaction.setCreatedAt(LocalDateTime.now());
        mockTransaction.setStatus(Status.PENDING);

        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        Transaction result = transactionService.transferFunds(sender, recipient, amount);

        // Assert
        Assertions.assertNotNull(result, "Transaction should not be null");
        Assertions.assertEquals(Status.PENDING, result.getStatus(), "Transaction should be marked as PENDING");

        verify(transactionRepository, times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    void transferFundsVerified_Should_Throw_When_TokenDoesNotMatch() {
        // Arrange
        User sender = createMockUser();
        sender.setVerificationToken("valid-token");

        User recipient = createMockUser();
        double amount = 15000.0;

        Transaction pendingTransaction = createMockTransaction();
        pendingTransaction.getSender().setVerificationToken("invalid-token");

        when(transactionRepository.findBySenderRecipientAmountAndStatus(sender, recipient, amount, Status.PENDING))
                .thenReturn(pendingTransaction);

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                transactionService.transferFundsVerified(sender, recipient, amount)
        );

        verify(transactionRepository, times(1))
                .findBySenderRecipientAmountAndStatus(sender, recipient, amount, Status.PENDING);
    }

    @Test
    void getAllTransactionsForUser_Should_Return_All_Transactions() {
        // Arrange
        User user = createMockAdminUser();

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> transactionService.getAllTransactionsForUser(user.getId()));

        // Verify interactions
        verify(transactionRepository, times(1)).findAllByUserId(user.getId());
    }

    @Test
    void getPaginatedTransactionsForUser_Should_Return_All_PaginatedTransactions() {
        // Act & Assert
        Assertions.assertDoesNotThrow(() -> transactionService.getPaginatedTransactions(1, 2));

        // Verify interactions
        verify(transactionRepository,
                times(1)).findAll(PageRequest.of(1, 2));
    }

    @Test
    void filterTransactions_Should_Return_All_Transactions() {
        // Arrange
        User user = createMockAdminUser();

        // Act & Assert
        Assertions.assertDoesNotThrow(() ->
                transactionService.filterTransactions(null, null, null, null, user));

        // Verify repository call
        verify(transactionRepository, times(1)).findAllByUserId(user.getId());
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
        verify(transactionRepository, times(1)).findAllByUserId(user.getId());
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
        verify(transactionRepository, times(1)).findAllByUserId(user.getId());
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
