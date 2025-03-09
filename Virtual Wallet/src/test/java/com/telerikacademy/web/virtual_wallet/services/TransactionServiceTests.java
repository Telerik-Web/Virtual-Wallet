package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.repositories.TransactionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;
}
