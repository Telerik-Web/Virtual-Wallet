package com.telerikacademy.web.virtual_wallet.models;

import lombok.Data;

@Data
public class TransactionDTOCreate {

    private double amount;

    private String type;

    private String value;
}
