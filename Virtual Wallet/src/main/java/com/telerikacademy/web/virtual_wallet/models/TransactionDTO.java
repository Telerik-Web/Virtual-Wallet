package com.telerikacademy.web.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.virtual_wallet.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDTO {

    @JsonIgnore
    private boolean isIncoming;

    private long sender_id;

    private long recipient_id;

    private double amount;

    private Status status;

    private LocalDateTime createdAt;

    private String transactionType;

    public TransactionDTO(boolean isIncoming,
                          long sender_id,
                          long recipient_id,
                          double amount,
                          Status status,
                          LocalDateTime createdAt) {
        this.isIncoming = isIncoming;
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.transactionType = isIncoming ? "RECEIVED" : "SENT";
        ;
    }
}
