package com.mintos.assignment.service;

import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.domain.repository.TransactionRepository;
import com.mintos.assignment.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void record_shouldSaveTransaction() {
        Transaction transaction = mock(Transaction.class);
        transactionService.record(transaction);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void getTransactions_shouldReturnList_whenValidArgs() {
        UUID accountId = UUID.randomUUID();
        List<Transaction> expected = Collections.singletonList(mock(Transaction.class));
        when(transactionRepository.findByAccountId(accountId, 0, 10)).thenReturn(expected);

        List<Transaction> result = transactionService.getTransactions(accountId, 0, 10);

        assertEquals(expected, result);
        verify(transactionRepository).findByAccountId(accountId, 0, 10);
    }

    @Test
    void getTransactions_shouldThrow_whenLimitTooLow() {
        UUID accountId = UUID.randomUUID();
        ApiException ex = assertThrows(ApiException.class,
            () -> transactionService.getTransactions(accountId, 0, 0));
        assertTrue(ex.getMessage().contains("Limit must be between 1 and 100"));
    }

    @Test
    void getTransactions_shouldThrow_whenLimitTooHigh() {
        UUID accountId = UUID.randomUUID();
        ApiException ex = assertThrows(ApiException.class,
            () -> transactionService.getTransactions(accountId, 0, 101));
        assertTrue(ex.getMessage().contains("Limit must be between 1 and 100"));
    }

    @Test
    void getTransactions_shouldThrow_whenOffsetNegative() {
        UUID accountId = UUID.randomUUID();
        ApiException ex = assertThrows(ApiException.class,
            () -> transactionService.getTransactions(accountId, -1, 10));
        assertTrue(ex.getMessage().contains("Offset must be non-negative"));
    }
}
