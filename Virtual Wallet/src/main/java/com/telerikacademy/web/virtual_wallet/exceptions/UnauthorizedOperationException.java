package com.telerikacademy.web.virtual_wallet.exceptions;

public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
