package com.mintos.assignment.exception;

public class InvalidTransferException extends ApiException {
    public InvalidTransferException(String message) {
        super(message, ErrorCodes.INVALID_TRANSFER);
    }
}