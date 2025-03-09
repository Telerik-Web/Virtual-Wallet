package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.TransactionRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

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
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferFunds(null, createMockUser(), 1.1));
    }

    @Test
    void transferFunds_Should_Throw_WhenRecipientIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferFunds(createMockUser(), null, 1.1));
    }

    @Test
    void transferFunds_Should_Throw_WhenAmountIsZero() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferFunds(createMockUser(), createMockUser2(), 0.0));
    }

    @Test
    void transferFunds_Should_Throw_WhenInsufficientFunds() {
        User user = createMockUser();
        user.setBalance(0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferFunds(user, createMockUser2(), 10.0));
    }

    @Test
    void transferFunds_Should_UpdateTransfer_When_Valid() {
        User user = createMockUser();
        User user2 = createMockUser2();
        transactionService.transferFunds(user, user2, 1.0);

        Assertions.assertEquals(99, user.getBalance());
        Assertions.assertEquals(51, user2.getBalance());

        Mockito.verify(userRepository, Mockito.times(1)).update(user, user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).update(user2, user2.getId());

        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void getAllTransactionsForUser_Should_Return_All_Transactions() {
        User user = createMockUser();
        Assertions.assertDoesNotThrow(() -> transactionService.getAllTransactionsForUser(user.getId()));
        Mockito.verify(transactionRepository, Mockito.times(1)).findAllByUserId(user.getId());
    }

    @Test
    void filterTransactions_Should_Return_All_Transactions() {
        User user = createMockUser();
        List<Transaction> transactions = new ArrayList<>();
        Assertions.assertDoesNotThrow(() ->
                transactionService.filterTransactions(null, null, null, null, user));
        Mockito.verify(transactionRepository, Mockito.times(1)).findAllByUserId(user.getId());
    }

    @Test
    void filterTransactions_Should_Return_All_IncomingTransactions() {
        User user = createMockUser();
        List<Transaction> transactions = new ArrayList<>();
        Transaction incoming = new Transaction();
        incoming.setRecipient(user);
        transactions.add(incoming);
        Assertions.assertDoesNotThrow(() ->
                transactionService.filterTransactions(null, null, null, true, user));
        Mockito.verify(transactionRepository, Mockito.times(1)).findAllByUserId(user.getId());
    }

    @Test
    void filterTransactions_Should_Return_All_OutgoingTransactions() {
        User user = createMockUser();
        List<Transaction> transactions = new ArrayList<>();
        Transaction outgoing = new Transaction();
        outgoing.setSender(user);
        transactions.add(outgoing);
        Assertions.assertDoesNotThrow(() ->
                transactionService.filterTransactions(null, null, null, false, user));
        Mockito.verify(transactionRepository, Mockito.times(1)).findAllByUserId(user.getId());
    }

    @Test
    void sortTransactions_ByAmount_Ascending() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        transactions.add(createMockTransaction2());

        List<Transaction> sorted = transactionService.sortTransactions2(transactions, "amount", true);

        Assertions.assertEquals(100, sorted.get(0).getAmount());
        Assertions.assertEquals(200, sorted.get(1).getAmount());
    }

    @Test
    void sortTransactions_ByAmount_Descending() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        transactions.add(createMockTransaction2());

        List<Transaction> sorted = transactionService.sortTransactions2(transactions, "amount", false);

        Assertions.assertEquals(200, sorted.get(0).getAmount());
        Assertions.assertEquals(100, sorted.get(1).getAmount());
    }

    @Test
    void sortTransactions_ByDate_Ascending() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        transactions.add(createMockTransaction2());

        List<Transaction> sorted = transactionService.sortTransactions2(transactions, "date", true);

        Assertions.assertEquals(LocalDateTime.of(2025, 3, 9, 17, 26), sorted.get(0).getCreatedAt());
        Assertions.assertEquals(LocalDateTime.of(2025, 4, 9, 17, 26), sorted.get(1).getCreatedAt());
    }
}
