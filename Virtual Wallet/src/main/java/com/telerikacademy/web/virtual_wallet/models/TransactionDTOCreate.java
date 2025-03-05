package com.telerikacademy.web.virtual_wallet.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionDTOCreate {

    @NotNull(message = "Amount must be above 0!")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero!")
    private double amount;

    private String type;

    @NotEmpty(message = "Recipient can't be empty!")
    private String value;
}
