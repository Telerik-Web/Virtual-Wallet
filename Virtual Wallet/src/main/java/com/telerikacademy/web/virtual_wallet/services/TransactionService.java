package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.TransactionDTO;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction transferFunds(User sender, User recipient, Double amount);

    Transaction transferFundsVerified(User sender, User recipient, Double amount);

    Page<Transaction> getPaginatedTransactions(int page, int size);

    List<Transaction> getAllTransactionsForUser(long userId);

    List<Transaction> filterTransactions(LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         String recipient,
                                         Boolean isIncoming,
                                         User user);

    List<TransactionDTO> sortTransactions(List<TransactionDTO> transactions,
                                       String sortBy,
                                       boolean ascending);

    Page<Transaction> sortTransactionsWithPagination(List<Transaction> transactions,
                                        String sortBy,
                                        boolean ascending,
                                        int page,
                                        int size);
}
