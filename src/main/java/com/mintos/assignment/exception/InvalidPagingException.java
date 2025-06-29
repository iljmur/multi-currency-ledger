package com.mintos.assignment.exception;

public class InvalidPagingException extends ApiException {
    public InvalidPagingException(String message) {
        super(message, ErrorCodes.INVALID_PAGING);
    }
}