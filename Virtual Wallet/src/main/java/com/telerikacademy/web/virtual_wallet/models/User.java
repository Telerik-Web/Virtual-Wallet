package com.telerikacademy.web.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.*;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_Name")
    private String firstName;

    @Column(name = "last_Name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "photo")
    private String photo;

    @Column(name = "isAdmin")
    private Boolean isAdmin;

    @Column(name = "isBlocked")
    private Boolean isBlocked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Card> cards = new ArrayList<>();

    @Column(name = "balance")
    private double balance;

    @Column(name = "verification_token", unique = true)
    private String verificationToken;

    @Column(name = "account_verified", nullable = false)
    private boolean accountVerified = false;

    public User() {
    }

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
