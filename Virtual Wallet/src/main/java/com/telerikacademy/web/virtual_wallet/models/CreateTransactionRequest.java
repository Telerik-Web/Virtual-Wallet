package com.telerikacademy.web.virtual_wallet.models;

import lombok.Data;

@Data
public class CreateTransactionRequest {

    private String cardNumber;
    private double amount;
}
