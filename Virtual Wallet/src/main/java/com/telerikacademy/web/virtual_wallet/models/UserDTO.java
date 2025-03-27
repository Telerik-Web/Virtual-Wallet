package com.telerikacademy.web.virtual_wallet.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols.")
    private String username;

    @NotEmpty(message = "First name can't be empty.")
    @Size(min = 1, max = 16, message = "First name must be between 1 and 16 characters.")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty.")
    @Size(min = 1, max = 16, message = "Last name must be between 1 and 16 characters.")
    private String lastName;

    @NotEmpty(message = "Password can't be empty.")
    @Size(min = 8, message = "Password must be at least 8 symbols long.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character."
    )
    private String password;

    @NotEmpty(message = "Password confirm can't be empty.")
    @Size(min = 8, message = "Password confirm must be at least 8 symbols long.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password confirm must contain at least one uppercase letter, one digit, and one special character."
    )
    private String passwordConfirm;

    @NotEmpty(message = "Email can't be empty.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotEmpty(message = "Phone number can't be empty.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

}
