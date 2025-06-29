package com.mintos.assignment.exception;

public class ClientNotFoundException extends ApiException {
    public ClientNotFoundException(String message) {
        super(message, ErrorCodes.CLIENT_NOT_FOUND);
    }
}