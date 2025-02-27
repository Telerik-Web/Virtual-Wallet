package com.telerikacademy.web.virtual_wallet.models;

import com.telerikacademy.web.virtual_wallet.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(name = "amount")
    private double amount;

    @Column(name = "status")
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime timestamp;

}
