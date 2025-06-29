package com.mintos.assignment.exception;

public class ExchangeRateException extends ApiException {
    public ExchangeRateException(String message) {
        super(message, ErrorCodes.EXCHANGE_RATE_ERROR);
    }
}