package com.telerikacademy.web.virtual_wallet.models;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


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


    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
