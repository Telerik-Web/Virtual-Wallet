package com.telerikacademy.web.virtual_wallet.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterUserDto {

    private String username;
    private String phoneNumber;
    private String email;
}
