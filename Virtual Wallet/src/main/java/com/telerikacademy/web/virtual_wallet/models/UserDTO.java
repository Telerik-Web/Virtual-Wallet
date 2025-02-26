package com.telerikacademy.web.virtual_wallet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {

    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols.")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Password can't be empty.")
    @Size(min = 8, message = "Password must be at least 8 symbols long.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character."
    )
    private String password;

    @NotEmpty(message = "Email can't be empty.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotEmpty(message = "Phone number can't be empty.")
    @Pattern(
            regexp = "(?=.*\\d{10})",
            message = "Phone number must be 10 digits!"
    )
    @Column(unique = true, length = 10)
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
