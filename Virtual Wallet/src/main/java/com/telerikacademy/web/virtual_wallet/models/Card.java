package com.telerikacademy.web.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiration_Date")
    private LocalDate expirationDate;

    @Column(name = "check_Number")
    private String checkNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @PrePersist
    protected void onCreate() {
        if (expirationDate == null) {
            expirationDate = LocalDate.now().plusYears(5);
        }
    }

    @Transient
    public String getCardHolder() {
        return user.getFullName();
    }

}