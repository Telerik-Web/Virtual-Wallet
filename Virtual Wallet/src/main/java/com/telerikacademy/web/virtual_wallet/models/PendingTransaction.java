package com.telerikacademy.web.virtual_wallet.models;

import lombok.Data;

@Data
public class PendingTransaction {
        private final User sender;
        private final User recipient;
        private final double amount;
}
