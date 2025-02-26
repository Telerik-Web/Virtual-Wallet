package com.telerikacademy.web.virtual_wallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Card number can't be empty!")
    @Pattern(
            regexp = ".\\d{16}",
            message = "Card number must be 16 digits!"
    )
    @Column(unique = true, length = 16)
    private String cardNumber;

    @NotEmpty(message = "Expiration date can't be empty!")
    @Future(message = "Expiration date must be in the future.")
    private String expiryDate;

    @NotEmpty(message = "Card holder name can't be empty!")
    @Size(min = 2, max = 30, message = "Card holder name must be between 2 and 30 symbols!")
    private String cardHolderName;

    @NotEmpty(message = "CVV can't be empty!")
    @Pattern(
            regexp = ".\\d{3}",
            message = "CVV must be 3 digits!"
    )
    @Column(length = 3)
    private String cvv;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
