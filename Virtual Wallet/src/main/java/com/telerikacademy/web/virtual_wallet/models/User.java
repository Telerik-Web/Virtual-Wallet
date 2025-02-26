package com.telerikacademy.web.virtual_wallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols.")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "First name can't be empty.")
    @Size(min = 1, max = 16, message = "First name must be between 1 and 16 characters.")
    @Column(name = "first_Name")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty.")
    @Size(min = 1, max = 16, message = "Last name must be between 1 and 16 characters.")
    @Column(name = "last_Name")
    private String lastName;

    @NotEmpty(message = "Password can't be empty.")
    @Size(min = 8, message = "Password must be at least 8 symbols long.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character."
    )
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Email can't be empty.")
    @Email(message = "Email must be valid.")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Phone number can't be empty.")
    @Pattern(
            regexp = "(?=.*\\d{10})",
            message = "Phone number must be 10 digits!"
    )
    @Column(name = "phone")
    private String phone;

    @NotEmpty(message = "Card can't be empty.")
    @Pattern(
            regexp = ".\\d{16}",
            message = "Card number must be 16 digits!"
    )

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "isAdmin")
    private Boolean isAdmin;

    @Column(name = "isBlocked")
    private Boolean isBlocked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Card> cards;

    @AssertTrue(message = "Full name must be between 2 and 32 characters.")
    public boolean isFullNameValid() {
        int length = (firstName + " " + lastName).trim().length();
        return length >= 2 && length <= 32;
    }

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

}
