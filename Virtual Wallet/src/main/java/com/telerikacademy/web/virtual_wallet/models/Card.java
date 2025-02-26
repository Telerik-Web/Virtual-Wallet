package com.telerikacademy.web.virtual_wallet.models;

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

    @NotNull(message = "Card number is required.")
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be exactly 16 digits.")
    @Column(name = "card_number")
    private String cardNumber;

    @NotNull(message = "Expiration date is required.")
    @Column(name = "expiration_Date")
    private LocalDate expirationDate;

    @NotNull(message = "Check number is required.")
    @Pattern(regexp = "^\\d{3}$", message = "Check number must be exactly 3 digits.")
    @Column(name = "check_Number")
    private String checkNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
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