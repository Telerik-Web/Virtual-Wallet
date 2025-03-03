package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.TransactionDTO;
import com.telerikacademy.web.virtual_wallet.models.TransactionFilter;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    List<Transaction> findBySender(User sender);

    List<Transaction> findByRecipient(User recipient);

    List<Transaction> findBySenderAndRecipient(User sender, User recipient);

    List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByStatus(Status status);

    List<Transaction> findByAmountGreaterThan(BigDecimal amount);

    List<Transaction> findByAmountLessThan(BigDecimal amount);

    List<Transaction> findBySenderAndStatus(User sender, Status status);

    List<Transaction> findByRecipientAndStatus(User recipient, Status status);

    Transaction transferFunds(User sender, User recipient, Double amount);

    List<Transaction> getAllTransactionsForUser(long userId);

    List<Transaction> filterTransactions(LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         String recipient,
                                         Boolean isIncoming,
                                         User user);

    List<TransactionDTO> sortTransactions(List<TransactionDTO> transactions,
                                       String sortBy,
                                       boolean ascending);
}
