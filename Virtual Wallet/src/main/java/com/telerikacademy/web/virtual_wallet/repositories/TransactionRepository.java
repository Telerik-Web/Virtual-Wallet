package com.telerikacademy.web.virtual_wallet.repositories;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :userId OR t.recipient.id = :userId")
    List<Transaction> findAllByUserId(@Param("userId") long userId);

    @Query("SELECT t FROM Transaction t WHERE t.sender = :sender AND t.recipient = :recipient " +
            "AND t.amount = :amount AND t.status = :status")
    Transaction findBySenderRecipientAmountAndStatus(@Param("sender") User sender,
                                               @Param("recipient") User recipient,
                                               @Param("amount") double amount,
                                               @Param("status") Status status);

}
