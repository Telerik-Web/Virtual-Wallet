package com.telerikacademy.web.virtual_wallet.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LogInDto {

    @NotEmpty(message = "Username can't be empty!")
    private String username;

    @NotEmpty(message = "Password can't be empty!")
    private String password;
}
