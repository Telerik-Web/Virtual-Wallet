package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.TransactionDTO;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.TransactionRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

//    @Override
//    public List<Transaction> findBySender(User sender) {
//        return transactionRepository.findBySender(sender);
//    }
//
//    @Override
//    public List<Transaction> findByRecipient(User recipient) {
//        return transactionRepository.findByRecipient(recipient);
//    }
//
//    @Override
//    public List<Transaction> findBySenderAndRecipient(User sender, User recipient) {
//        return transactionRepository.findBySenderAndRecipient(sender, recipient);
//    }
//
//    @Override
//    public List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
//        return transactionRepository.findByCreatedAtBetween(startDate, endDate);
//    }
//
//    @Override
//    public List<Transaction> findByStatus(Status status) {
//        return transactionRepository.findByStatus(status);
//    }
//
//    @Override
//    public List<Transaction> findByAmountGreaterThan(BigDecimal amount) {
//        return transactionRepository.findByAmountGreaterThan(amount);
//    }
//
//    @Override
//    public List<Transaction> findByAmountLessThan(BigDecimal amount) {
//        return transactionRepository.findByAmountLessThan(amount);
//    }
//
//    @Override
//    public List<Transaction> findBySenderAndStatus(User sender, Status status) {
//        return transactionRepository.findBySenderAndStatus(sender, status);
//    }
//
//    @Override
//    public List<Transaction> findByRecipientAndStatus(User recipient, Status status) {
//        return transactionRepository.findByRecipientAndStatus(recipient, status);
//    }

    @Override
    @Transactional
    public Transaction transferFunds(User sender, User recipient, Double amount) {

        if (sender == null || recipient == null) {
            throw new IllegalArgumentException("Sender and recipient must be provided.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Sender balance must be greater than zero.");
        }

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        userRepository.update(sender, sender.getId());
        userRepository.update(recipient, recipient.getId());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setStatus(Status.COMPLETED);
        transaction.setSender(sender);
        transaction.setRecipient(recipient);
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionsForUser(long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    @Override
    public List<Transaction> filterTransactions(LocalDateTime startDate,
                                                LocalDateTime endDate,
                                                String recipient,
                                                Boolean isIncoming,
                                                User user) {
        List<Transaction> allTransactions = transactionRepository.findAllByUserId(user.getId());
        return allTransactions.stream()
                .filter(t -> startDate == null || t.getCreatedAt().isAfter(startDate))
                .filter(t -> endDate == null || t.getCreatedAt().isBefore(endDate))
                .filter(t -> recipient == null || t.getRecipient().getUsername().equalsIgnoreCase(recipient))
//                .filter(t -> user.getUsername() == null || t.getSender().getUsername().equalsIgnoreCase(user.getUsername()))
                .filter(t -> {
                    if (isIncoming == null) return true;
                    boolean transactionIsIncoming = t.getRecipient().getId() == (user.getId());
                    return isIncoming == transactionIsIncoming;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> sortTransactions(List<TransactionDTO> transactions,
                                                 String sortBy,
                                                 boolean ascending) {
        Comparator<TransactionDTO> comparator = switch (sortBy.toLowerCase()) {
            case "amount" -> Comparator.comparing(TransactionDTO::getAmount);
            case "date" -> Comparator.comparing(TransactionDTO::getCreatedAt);
            default -> throw new IllegalStateException("Unexpected value: " + sortBy);
        };

        if (!ascending) {
            comparator = comparator.reversed();
        }

        return transactions
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> sortTransactions2(List<Transaction> transactions,
                                                 String sortBy,
                                                 boolean ascending) {
        Comparator<Transaction> comparator = switch (sortBy.toLowerCase()) {
            case "amount" -> Comparator.comparing(Transaction::getAmount);
            case "date" -> Comparator.comparing(Transaction::getCreatedAt);
            default -> throw new IllegalStateException("Unexpected value: " + sortBy);
        };

        if (!ascending) {
            comparator = comparator.reversed();
        }

        return transactions
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
