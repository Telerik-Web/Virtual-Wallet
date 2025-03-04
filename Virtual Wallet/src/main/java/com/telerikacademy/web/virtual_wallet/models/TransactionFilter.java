package com.telerikacademy.web.virtual_wallet.models;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionFilter {

    private double amount;
    private User recipient;
    private User sender;
    private Status status;
    private LocalDateTime createdAt;
    private boolean isIncoming;

    public TransactionFilter(double amount, LocalDateTime createdAt, User recipient, User sender, boolean isIncoming, Status status) {
        this.amount = amount;
        this.createdAt = createdAt;
        this.recipient = recipient;
        this.sender = sender;
        this.isIncoming = isIncoming;
        this.status = status;
    }

    public TransactionFilter() {
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", date=" + createdAt +
                ", recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                ", isIncoming=" + (isIncoming ? "Incoming" : "Outgoing") +
                '}';
    }
}
