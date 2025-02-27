package com.telerikacademy.web.virtual_wallet.models;

import lombok.Data;

import java.util.Optional;

@Data
public class FilterUserOptions {

    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> phoneNumber;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterUserOptions() {
        this(null, null, null, null, null);
    }

    public FilterUserOptions(String username, String email,
                             String phoneNumber, String sortBy, String sortOrder) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.phoneNumber = Optional.ofNullable(phoneNumber);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }
}
