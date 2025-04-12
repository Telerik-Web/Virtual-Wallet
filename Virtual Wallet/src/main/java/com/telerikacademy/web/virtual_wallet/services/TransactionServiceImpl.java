package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.TransactionDTO;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.TransactionRepository;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            throw new IllegalArgumentException("Sender does not have enough balance.");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(sender);
        transaction.setRecipient(recipient);
        transaction.setCreatedAt(LocalDateTime.now());

        if (amount >= 10000) {
            transaction.setStatus(Status.PENDING);
        } else {
            sender.setBalance(sender.getBalance() - amount);
            recipient.setBalance(recipient.getBalance() + amount);

            userRepository.save(sender);
            userRepository.save(recipient);

            transaction.setStatus(Status.COMPLETED);
        }

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction transferFundsVerified(User sender, User recipient, Double amount) {

        Transaction transaction = transactionRepository.findBySenderRecipientAmountAndStatus
                        (sender, recipient, amount, Status.PENDING);
        if (!transaction.getSender().getVerificationToken().equals(sender.getVerificationToken())
                || transaction.getAmount() != amount) {
            throw new EntityNotFoundException("transaction", transaction.getId());
        }
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(recipient);

        transaction.setStatus(Status.COMPLETED);
        return transactionRepository.save(transaction);
    }


    @Override
    public Page<Transaction> getPaginatedTransactions(int page, int size) {
        return transactionRepository.findAll(PageRequest.of(page, size));
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
                .filter(t -> {
                    if (isIncoming == null) return true;
                    boolean transactionIsIncoming = t.getRecipient().getId() == ((user.getId()));
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
    public Page<Transaction> sortTransactionsWithPagination(List<Transaction> transactions,
                                                            String sortBy,
                                                            boolean ascending,
                                                            int page,
                                                            int size) {
        Comparator<Transaction> comparator = switch (sortBy.toLowerCase()) {
            case "amount" -> Comparator.comparing(Transaction::getAmount);
            case "date" -> Comparator.comparing(Transaction::getCreatedAt);
            default -> throw new IllegalStateException("Unexpected value: " + sortBy);
        };

        if (!ascending) {
            comparator = comparator.reversed();
        }


        List<Transaction> sortedTransactions = transactions.stream()
                .sorted(comparator)
                .collect(Collectors.toList());


        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedTransactions.size());

        List<Transaction> paginatedTransactions = sortedTransactions.subList(start, end);

        return new PageImpl<>(paginatedTransactions, pageable, sortedTransactions.size());
    }
}
