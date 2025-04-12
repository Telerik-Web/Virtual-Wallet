package com.telerikacademy.web.virtual_wallet.models.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTOOut {

    private long id;
    private String username;
    private String email;
    private String phone;

    public UserDTOOut() {
    }

    public UserDTOOut(Integer id, String username, String email, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }
}
