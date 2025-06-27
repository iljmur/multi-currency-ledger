package com.mintos.assignment.domain.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final UUID clientId;
    private final Currency currency;
    private BigDecimal balance;

    public Account(UUID id, UUID clientId, Currency currency, BigDecimal balance) {
        this.id = id;
        this.clientId = clientId;
        this.currency = currency;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot deposit a negative amount.");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot withdraw a negative amount.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds.");
        }
        this.balance = this.balance.subtract(amount);
    }
}
