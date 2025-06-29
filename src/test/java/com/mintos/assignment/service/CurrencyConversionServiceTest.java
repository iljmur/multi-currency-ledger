package com.mintos.assignment.service;

import com.mintos.assignment.infrastructure.client.ExchangeRateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyConversionServiceTest {

    private ExchangeRateClient exchangeRateClient;
    private CurrencyConversionService conversionService;

    @BeforeEach
    void setUp() {
        exchangeRateClient = mock(ExchangeRateClient.class);
        conversionService = new CurrencyConversionService(exchangeRateClient);
    }

    @Test
    void convert_shouldReturnSameAmount_whenCurrenciesAreEqual() {
        Currency eur = Currency.getInstance("EUR");
        BigDecimal amount = new BigDecimal("123.45");

        BigDecimal result = conversionService.convert(amount, eur, eur);

        assertEquals(amount, result);
        verify(exchangeRateClient, never()).getExchangeRate(any(), any());
    }

    @Test
    void convert_shouldUseExchangeRate_whenCurrenciesAreDifferent() {
        Currency usd = Currency.getInstance("USD");
        Currency eur = Currency.getInstance("EUR");
        BigDecimal amount = new BigDecimal("100.00");

        when(exchangeRateClient.getExchangeRate("USD", "EUR"))
            .thenReturn(new BigDecimal("0.90"));

        BigDecimal result = conversionService.convert(amount, usd, eur);

        assertEquals(new BigDecimal("90.00"), result);
        verify(exchangeRateClient).getExchangeRate("USD", "EUR");
    }

    @Test
    void convert_shouldRoundToTwoDecimalPlaces() {
        Currency usd = Currency.getInstance("USD");
        Currency chf = Currency.getInstance("CHF");
        BigDecimal amount = new BigDecimal("123.4567");

        when(exchangeRateClient.getExchangeRate("USD", "CHF"))
            .thenReturn(new BigDecimal("0.98765"));

        BigDecimal result = conversionService.convert(amount, usd, chf);

        assertEquals(new BigDecimal("121.93"), result); // 121.925 Rounded HALF_UP
    }
}
