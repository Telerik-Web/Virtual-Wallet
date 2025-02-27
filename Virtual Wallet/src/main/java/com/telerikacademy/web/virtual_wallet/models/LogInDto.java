package com.telerikacademy.web.virtual_wallet.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LogInDto {

    @NotEmpty(message = "Username cannot be empty!")
    private String username;

    @NotEmpty(message = "Password cannot be empty!")
    private String password;
}
