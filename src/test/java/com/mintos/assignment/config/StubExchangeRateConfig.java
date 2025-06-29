package com.mintos.assignment.config;

import com.mintos.assignment.infrastructure.client.ExchangeRateClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@TestConfiguration
public class StubExchangeRateConfig {

    @Bean
    public ExchangeRateClient exchangeRateClient() {
        return new ExchangeRateClient(null, null) {
            @Override
            public BigDecimal getExchangeRate(String from, String to) {
                if ("USD".equals(from) && "EUR".equals(to)) {
                    return new BigDecimal("0.90");
                }
                if ("USD".equals(from) && "CHF".equals(to)) {
                    return new BigDecimal("0.95");
                }
                if ("EUR".equals(from) && "USD".equals(to)) {
                    return new BigDecimal("1.10");
                }
                return BigDecimal.ONE;
            }
        };
    }
}
