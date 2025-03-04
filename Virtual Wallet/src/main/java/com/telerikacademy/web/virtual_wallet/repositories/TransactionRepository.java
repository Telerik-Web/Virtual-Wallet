package com.telerikacademy.web.virtual_wallet.repositories;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.TransactionFilter;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 1. Get all transactions by sender
    List<Transaction> findBySender(User sender);

    // 2. Get all transactions by recipient
    List<Transaction> findByRecipient(User recipient);

    // 3. Get transactions filtered by both sender and recipient
    List<Transaction> findBySenderAndRecipient(User sender, User recipient);

    // 4. Get transactions within a date range
    List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 5. Get transactions by status (e.g., PENDING, COMPLETED, FAILED)
    List<Transaction> findByStatus(Status status);

    // 6. Get transactions by amount greater than or less than a certain value
    List<Transaction> findByAmountGreaterThan(BigDecimal amount);
    List<Transaction> findByAmountLessThan(BigDecimal amount);

    // 7. Get transactions filtered by sender and status
    List<Transaction> findBySenderAndStatus(User sender, Status status);

    // 8. Get transactions filtered by recipient and status
    List<Transaction> findByRecipientAndStatus(User recipient, Status status);

    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :userId OR t.recipient.id = :userId")
    List<Transaction> findAllByUserId(@Param("userId") long userId);

}
