package com.telerikacademy.web.virtual_wallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phoneNumber")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols.")
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
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Phone number can't be empty.")
    @Pattern(
            regexp = "(?=.*\\d{10})",
            message = "Phone number must be 10 digits!"
    )
    @Column(nullable = false, unique = true)
    private String phone;

    @NotEmpty(message = "Card can't be empty.")
    @Pattern(
            regexp = ".\\d{16}",
            message = "Card number must be 16 digits!"
    )
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, unique = true)
    private Card card;

    @Lob
    private byte[] photo;

    private boolean isAdmin;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
