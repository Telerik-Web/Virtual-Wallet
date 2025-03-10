package com.telerikacademy.web.virtual_wallet.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "secureTokens")
public class SecureToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(unique = true)
    private String name;
}
