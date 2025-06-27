package com.mintos.assignment.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal balance;

    protected AccountEntity() {
        // For JPA
    }

    public AccountEntity(UUID id, UUID clientId, String currency, BigDecimal balance) {
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

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
