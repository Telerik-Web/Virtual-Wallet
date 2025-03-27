package com.telerikacademy.web.virtual_wallet.mappers;
import com.telerikacademy.web.virtual_wallet.repositories.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    TransactionRepository transactionRepository;

    public TransactionMapper(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
