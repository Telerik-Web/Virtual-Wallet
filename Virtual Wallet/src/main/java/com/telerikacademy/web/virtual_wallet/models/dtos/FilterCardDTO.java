package com.telerikacademy.web.virtual_wallet.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCardDTO {

    private String cardNumber;
    private String sortOrder;
    private String sortBy;
    private String orderBy;
}
