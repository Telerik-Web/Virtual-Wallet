package com.telerikacademy.web.virtual_wallet.models;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterDto extends LogInDto {
    @NotEmpty(message = "Password confirmation cannot be empty!")
    private String passwordConfirm;

    @NotEmpty(message = "Username can't be empty.")
    private String username;

    @NotEmpty(message = "Email can't be empty.")
    @Email(message = "Must be a valid email.")
    private String email;

    @NotEmpty(message = "Phone number can't be empty.")
    private String phone;

}
