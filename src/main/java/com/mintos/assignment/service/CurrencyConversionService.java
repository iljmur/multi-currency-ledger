package com.mintos.assignment.service;

import com.mintos.assignment.infrastructure.client.ExchangeRateClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Service
public class CurrencyConversionService {

    private final ExchangeRateClient exchangeRateClient;

    public CurrencyConversionService(ExchangeRateClient exchangeRateClient) {
        this.exchangeRateClient = exchangeRateClient;
    }

    /**
     * Converts the given amount from one currency to another.
     *
     * @param amount     the original amount
     * @param from       currency to convert from
     * @param to         currency to convert to
     * @return converted amount in target currency
     */
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (from.equals(to)) {
            return amount;
        }

        BigDecimal rate = exchangeRateClient.getExchangeRate(from.getCurrencyCode(), to.getCurrencyCode());
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
