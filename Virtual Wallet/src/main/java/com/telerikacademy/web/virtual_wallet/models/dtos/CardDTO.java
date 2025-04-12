package com.telerikacademy.web.virtual_wallet.models.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDTO {

    @NotNull(message = "Card number is required.")
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be exactly 16 digits.")
    private String cardNumber;

    @NotNull(message = "Check number is required.")
    @Pattern(regexp = "^\\d{3}$", message = "Check number must be exactly 3 digits.")
    @Column(name = "check_Number")
    private String checkNumber;



}
