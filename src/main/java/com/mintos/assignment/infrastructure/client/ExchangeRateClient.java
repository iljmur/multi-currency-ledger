package com.mintos.assignment.infrastructure.client;

import com.mintos.assignment.exception.ExchangeRateException;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class ExchangeRateClient {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;

    public ExchangeRateClient(RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    /**
     * Fetches the exchange rate from one currency to another using the Frankfurter public API.
     * <p>
     * Example request:
     * {@code https://api.frankfurter.app/latest?from=USD&to=EUR}
     * <p>
     * Successful response (HTTP 200):
     * <pre>
     * {
     *   "amount": 1.0,
     *   "base": "USD",
     *   "date": "2025-06-27",
     *   "rates": {
     *     "EUR": 0.85441
     *   }
     * }
     * </pre>
     * <p>
     * Error response (e.g. unsupported currency):
     * <pre>
     * {
     *   "message": "not found"
     * }
     * </pre>
     *
     * @param from base currency (e.g., "USD")
     * @param to target currency (e.g., "EUR")
     * @return conversion rate as BigDecimal
     * @throws com.mintos.assignment.exception.ExchangeRateException
     *         if API call fails or rate is missing/invalid
     */
    public BigDecimal getExchangeRate(String from, String to) {
        return retryTemplate.execute(context -> {
            String url = String.format("https://api.frankfurter.app/latest?from=%s&to=%s", from, to);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ExchangeRateException("Failed to fetch exchange rate from " + from + " to " + to);
            }

            Object ratesObj = response.getBody().get("rates");
            if (!(ratesObj instanceof Map)) {
                throw new ExchangeRateException("Missing or malformed 'rates' field in response");
            }

            Map<String, Object> rates = (Map<String, Object>) ratesObj;
            Object rate = rates.get(to);
            if (rate instanceof Number) {
                return new BigDecimal(rate.toString());
            }

            throw new ExchangeRateException("Exchange rate not found for " + from + " to " + to);
        });
    }
}
