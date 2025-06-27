package com.mintos.assignment.domain.repository;

import com.mintos.assignment.domain.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findByAccountId(UUID accountId, int offset, int limit);
}
