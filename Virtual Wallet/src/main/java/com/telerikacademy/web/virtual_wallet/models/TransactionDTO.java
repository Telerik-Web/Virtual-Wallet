package com.telerikacademy.web.virtual_wallet.models;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.security.Timestamp;

@Data
public class TransactionDTO {

    private User sender_id;

    private User recipient_id;

    private double amount;

    private Status status;

    private Timestamp timestamp;
}
