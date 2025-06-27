package com.mintos.assignment.infrastructure.client;

import org.springframework.retry.support.RetryTemplate;
import org.springframework.http.ResponseEntity;
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
     * Fetches the exchange rate from one currency to another using exchangerate.host API.
     *
     * @param from base currency (e.g., \"USD\")
     * @param to target currency (e.g., \"EUR\")
     * @return conversion rate
     */
    public BigDecimal getExchangeRate(String from, String to) {
        return retryTemplate.execute(context -> {
            String url = String.format(
                "https://api.exchangerate.host/latest?base=%s&symbols=%s",
                from, to
            );

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Failed to fetch exchange rate");
            }

            Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
            Object rate = rates.get(to);

            if (rate instanceof Number) {
                return new BigDecimal(rate.toString());
            }

            throw new RuntimeException("Invalid exchange rate format for " + to);
        });
    }
}
