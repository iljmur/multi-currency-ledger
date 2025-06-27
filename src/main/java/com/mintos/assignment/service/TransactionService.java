package com.mintos.assignment.service;

import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.domain.repository.TransactionRepository;
import com.mintos.assignment.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void record(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(UUID accountId, int offset, int limit) {
        if (limit < 1 || limit > 100) {
            throw new ApiException("Limit must be between 1 and 100");
        }
        if (offset < 0) {
            throw new ApiException("Offset must be non-negative");
        }

        return transactionRepository.findByAccountId(accountId, offset, limit);
    }
}
