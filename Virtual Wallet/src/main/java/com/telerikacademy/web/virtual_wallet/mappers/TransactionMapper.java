package com.telerikacademy.web.virtual_wallet.mappers;

import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.TransactionFilter;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.models.UserDTO;
import com.telerikacademy.web.virtual_wallet.repositories.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    TransactionRepository transactionRepository;

    public TransactionMapper(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

//    public TransactionFilter fromTransaction(Transaction transaction) {
//        TransactionFilter transactionFilter = new TransactionFilter();
//        transactionFilter.setAmount(transaction.getAmount());
//        transactionFilter.setDate(transaction.getCreatedAt());
//        transactionFilter.setIncoming(true);
//        transactionFilter.setSender(transaction.getSender().getUsername());
//        transactionFilter.setRecipient(transaction.getRecipient().getUsername());
//        return transactionFilter;
//    }
}
