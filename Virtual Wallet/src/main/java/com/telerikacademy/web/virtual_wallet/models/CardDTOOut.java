package com.telerikacademy.web.virtual_wallet.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CardDTOOut {

    private long id;

    private String cardNumber;

    private LocalDate expirationDate;

    private String checkNumber;

    private String cardHolder;
}
