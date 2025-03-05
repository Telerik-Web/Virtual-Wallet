package com.telerikacademy.web.virtual_wallet.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDtoOut {

    private long id;
    private String username;
    private String email;
    private String phone;

    public UserDtoOut() {
    }

    public UserDtoOut(Integer id, String username, String email, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }
}
