package com.telerikacademy.web.virtual_wallet.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCardDto {

    private String cardNumber;
    private String sortOrder;
    private String sortBy;
    private String orderBy;
}
