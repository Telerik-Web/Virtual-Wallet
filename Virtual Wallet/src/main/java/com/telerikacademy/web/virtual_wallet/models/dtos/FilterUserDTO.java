package com.telerikacademy.web.virtual_wallet.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterUserDTO {

    private String username;
    private String phoneNumber;
    private String email;
}
