package com.mintos.assignment.config;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.model.Client;
import com.mintos.assignment.domain.repository.AccountRepository;
import com.mintos.assignment.domain.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(ClientRepository clientRepository, AccountRepository accountRepository) {
        return args -> {
            Client alice = new Client(UUID.randomUUID(), "Alice", "alice@mintos.com");
            Client bob = new Client(UUID.randomUUID(), "Bob", "bob@mintos.com");
            Client eve = new Client(UUID.randomUUID(), "Eve", "eve@mintos.com");

            clientRepository.save(alice);
            clientRepository.save(bob);
            clientRepository.save(eve);

            accountRepository.save(new Account(UUID.randomUUID(), alice.getId(), Currency.getInstance("EUR"), new BigDecimal("1000.00")));
            accountRepository.save(new Account(UUID.randomUUID(), alice.getId(), Currency.getInstance("USD"), new BigDecimal("500.00")));
            accountRepository.save(new Account(UUID.randomUUID(), alice.getId(), Currency.getInstance("CHF"), new BigDecimal("300.00")));

            accountRepository.save(new Account(UUID.randomUUID(), bob.getId(), Currency.getInstance("EUR"), new BigDecimal("700.00")));
            accountRepository.save(new Account(UUID.randomUUID(), bob.getId(), Currency.getInstance("USD"), new BigDecimal("400.00")));
            accountRepository.save(new Account(UUID.randomUUID(), bob.getId(), Currency.getInstance("CHF"), new BigDecimal("100.00")));

            accountRepository.save(new Account(UUID.randomUUID(), eve.getId(), Currency.getInstance("EUR"), new BigDecimal("250.00")));
            accountRepository.save(new Account(UUID.randomUUID(), eve.getId(), Currency.getInstance("USD"), new BigDecimal("120.00")));
            accountRepository.save(new Account(UUID.randomUUID(), eve.getId(), Currency.getInstance("CHF"), new BigDecimal("330.00")));

            System.out.println("✅ Demo clients and accounts seeded");
        };
    }
}
