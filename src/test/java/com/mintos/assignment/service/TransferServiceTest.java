package com.mintos.assignment.service;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    private AccountService accountService;
    private CurrencyConversionService currencyConversionService;
    private TransactionService transactionService;
    private TransferService transferService;

    private final UUID fromAccountId = UUID.randomUUID();
    private final UUID toAccountId = UUID.randomUUID();
    private final Currency usd = Currency.getInstance("USD");
    private final Currency eur = Currency.getInstance("EUR");

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        currencyConversionService = mock(CurrencyConversionService.class);
        transactionService = mock(TransactionService.class);

        transferService = new TransferService(accountService, currencyConversionService, transactionService);

        fromAccount = new Account(fromAccountId, UUID.randomUUID(), usd, BigDecimal.valueOf(1000));
        toAccount = new Account(toAccountId, UUID.randomUUID(), usd, BigDecimal.valueOf(500));
    }

    @Test
    void transfer_shouldSucceedWithSameCurrency() {
        when(accountService.getAccount(fromAccountId)).thenReturn(fromAccount);
        when(accountService.getAccount(toAccountId)).thenReturn(toAccount);

        transferService.transfer(fromAccountId, toAccountId, BigDecimal.valueOf(100), usd);

        verify(accountService).withdraw(fromAccount, BigDecimal.valueOf(100));
        verify(accountService).deposit(toAccount, BigDecimal.valueOf(100));
        verify(transactionService).record(any(Transaction.class));
    }

    @Test
    void transfer_shouldSucceedWithCurrencyConversion() {
        toAccount = new Account(toAccountId, UUID.randomUUID(), eur, BigDecimal.valueOf(500));
        when(accountService.getAccount(fromAccountId)).thenReturn(fromAccount);
        when(accountService.getAccount(toAccountId)).thenReturn(toAccount);
        when(currencyConversionService.convert(BigDecimal.valueOf(100), usd, eur))
            .thenReturn(BigDecimal.valueOf(92.50));

        transferService.transfer(fromAccountId, toAccountId, BigDecimal.valueOf(100), usd);

        verify(accountService).withdraw(fromAccount, BigDecimal.valueOf(100));
        verify(accountService).deposit(toAccount, BigDecimal.valueOf(92.50));
        verify(transactionService).record(any(Transaction.class));
    }

    @Test
    void transfer_shouldFailForNullAmount() {
        assertThrows(ApiException.class, () ->
            transferService.transfer(fromAccountId, toAccountId, null, usd)
        );
    }

    @Test
    void transfer_shouldFailForNegativeAmount() {
        assertThrows(ApiException.class, () ->
            transferService.transfer(fromAccountId, toAccountId, BigDecimal.valueOf(-5), usd)
        );
    }

    @Test
    void transfer_shouldFailIfSourceCurrencyMismatch() {
        fromAccount = new Account(fromAccountId, UUID.randomUUID(), eur, BigDecimal.valueOf(1000));
        when(accountService.getAccount(fromAccountId)).thenReturn(fromAccount);
        when(accountService.getAccount(toAccountId)).thenReturn(toAccount);

        ApiException exception = assertThrows(ApiException.class, () ->
            transferService.transfer(fromAccountId, toAccountId, BigDecimal.valueOf(100), usd)
        );

        assertTrue(exception.getMessage().contains("Requested transfer currency"));
    }
}
