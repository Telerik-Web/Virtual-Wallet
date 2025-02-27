package com.telerikacademy.web.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

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

    @Lob
    @JsonIgnore
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "isAdmin")
    private Boolean isAdmin;

    @Column(name = "isBlocked")
    private Boolean isBlocked;

    //    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    @JoinTable(name = "users_cards",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "card_id")
//    )
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private Set<Card> cards;

    @Column(name = "balance")
    private double Balance;

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

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
