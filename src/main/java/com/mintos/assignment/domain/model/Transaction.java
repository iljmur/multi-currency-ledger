package com.mintos.assignment.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final BigDecimal amount;
    private final Currency currency;
    private final Instant timestamp;

    public Transaction(UUID id, UUID sourceAccountId, UUID targetAccountId, BigDecimal amount, Currency currency, Instant timestamp) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSourceAccountId() {
        return sourceAccountId;
    }

    public UUID getTargetAccountId() {
        return targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
