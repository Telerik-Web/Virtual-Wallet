package com.telerikacademy.web.virtual_wallet.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TransactionDTOCreate {

    //@NotEmpty(message = "Amount must be above 0!")
    //@DecimalMin(value = "0.01", message = "Amount must be greater than zero!")
    @Pattern(regexp = "^(?!0(\\.0+)?$)\\d+(\\.\\d{1,2})?$", message = "Amount must be a positive number above 0!")
    private String amount;

    private String type;

    @NotEmpty(message = "Recipient can't be empty!")
    private String value;
}
