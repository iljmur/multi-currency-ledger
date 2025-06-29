package com.mintos.assignment.infrastructure.client;

import com.mintos.assignment.exception.ExchangeRateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ExchangeRateClientTest {

    private RestTemplate restTemplate;
    private RetryTemplate retryTemplate;
    private ExchangeRateClient client;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        retryTemplate = RetryTemplate.builder().build();
        client = new ExchangeRateClient(restTemplate, retryTemplate);
    }

    @Test
    void shouldReturnRate_whenResponseContainsValidNumber() {
        Map<String, Object> rates = new HashMap<>();
        rates.put("EUR", 1.23);
        Map<String, Object> body = Map.of("rates", rates);

        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
            .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        BigDecimal result = client.getExchangeRate("USD", "EUR");

        assertNotNull(result);
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void shouldThrow_whenStatusNot2xx() {
        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        assertThrows(ExchangeRateException.class, () ->
            client.getExchangeRate("USD", "EUR")
        );
    }

    @Test
    void shouldThrow_whenBodyIsNull() {
        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(ExchangeRateException.class, () ->
            client.getExchangeRate("USD", "EUR")
        );
    }

    @Test
    void shouldThrow_whenRatesKeyIsMissing() {
        Map<String, Object> body = Map.of("message", "not found");

        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
            .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        assertThrows(ExchangeRateException.class, () ->
            client.getExchangeRate("USD", "EUR")
        );
    }

    @Test
    void shouldThrow_whenRateIsMissing() {
        Map<String, Object> body = Map.of("rates", new HashMap<>());

        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
            .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        assertThrows(ExchangeRateException.class, () ->
            client.getExchangeRate("USD", "EUR")
        );
    }

    @Test
    void shouldThrow_whenRateIsInvalidType() {
        Map<String, Object> rates = Map.of("EUR", "abc"); // not a number
        Map<String, Object> body = Map.of("rates", rates);

        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
            .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        assertThrows(ExchangeRateException.class, () ->
            client.getExchangeRate("USD", "EUR")
        );
    }
}