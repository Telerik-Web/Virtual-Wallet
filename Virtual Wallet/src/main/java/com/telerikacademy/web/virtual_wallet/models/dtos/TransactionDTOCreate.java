package com.telerikacademy.web.virtual_wallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TransactionDTOCreate {

    @Pattern(regexp = "^(?!0(\\.0+)?$)\\d+(\\.\\d{1,2})?$", message = "Amount must be a positive number above 0!")
    private String amount;

    private String type;

    @NotEmpty(message = "Recipient can't be empty!")
    private String value;
}
