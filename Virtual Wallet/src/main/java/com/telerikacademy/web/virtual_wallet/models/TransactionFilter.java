package com.telerikacademy.web.virtual_wallet.models;

import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class TransactionFilter {

    private double amount;
    private LocalDateTime date;
    private String recipient;
    private String sender;
    private boolean isIncoming;

    public TransactionFilter(double amount, LocalDateTime date, String recipient, String sender, boolean isIncoming) {
        this.amount = amount;
        this.date = date;
        this.recipient = recipient;
        this.sender = sender;
        this.isIncoming = isIncoming;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", date=" + date +
                ", recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                ", isIncoming=" + (isIncoming ? "Incoming" : "Outgoing") +
                '}';
    }
}
