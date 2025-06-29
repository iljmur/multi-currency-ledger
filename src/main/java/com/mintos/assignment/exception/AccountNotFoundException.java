package com.mintos.assignment.exception;

public class AccountNotFoundException extends ApiException {
    public AccountNotFoundException(String message) {
        super(message, ErrorCodes.ACCOUNT_NOT_FOUND);
    }
}
