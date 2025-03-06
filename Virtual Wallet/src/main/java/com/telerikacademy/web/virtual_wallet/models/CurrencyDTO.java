package com.telerikacademy.web.virtual_wallet.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDTO {

    @NotEmpty(message = "Can't be empty!")
    private String from;

    @NotEmpty(message = "Can't be empty!")
    private String to;

    @NotEmpty(message = "Can't be empty!")
    private String amount;
}
