package com.telerikacademy.web.virtual_wallet.models;

import java.util.Optional;

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

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
