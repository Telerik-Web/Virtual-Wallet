package com.telerikacademy.web.virtual_wallet.models.dtos;

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
