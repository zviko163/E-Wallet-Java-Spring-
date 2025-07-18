package com.example.walletservices.exceptions;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String exception) {
        super(exception);
    }
}
