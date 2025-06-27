package com.mintos.assignment.service;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

@Service
public class TransferService {

    private final AccountService accountService;
    private final CurrencyConversionService currencyConversionService;
    private final TransactionService transactionService;

    public TransferService(AccountService accountService,
                           CurrencyConversionService currencyConversionService,
                           TransactionService transactionService) {
        this.accountService = accountService;
        this.currencyConversionService = currencyConversionService;
        this.transactionService = transactionService;
    }

    @Transactional
    public void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, Currency requestCurrency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Transfer amount must be positive");
        }

        Account source = accountService.getAccount(fromAccountId);
        Account target = accountService.getAccount(toAccountId);

        Currency sourceCurrency = source.getCurrency();
        Currency targetCurrency = target.getCurrency();

        if (!requestCurrency.equals(targetCurrency)) {
            throw new ApiException("Transfer currency must match receiver's account currency");
        }

        BigDecimal amountInSourceCurrency = requestCurrency.equals(sourceCurrency)
            ? amount
            : currencyConversionService.convert(amount, requestCurrency, sourceCurrency);

        accountService.withdraw(source, amountInSourceCurrency);
        accountService.deposit(target, amount);

        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            source.getId(),
            target.getId(),
            amount,
            targetCurrency,
            Instant.now()
        );
        transactionService.record(transaction);
    }
}
