package com.mintos.assignment.service;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.exception.ApiException;
import com.mintos.assignment.exception.InvalidTransferException;
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
            throw new InvalidTransferException("Transfer amount must be positive");
        }

        Account source = accountService.getAccount(fromAccountId);
        Account target = accountService.getAccount(toAccountId);

        Currency sourceCurrency = source.getCurrency();
        Currency targetCurrency = target.getCurrency();

        // Check that request currency matches source account currency
        if (!requestCurrency.equals(sourceCurrency)) {
            throw new InvalidTransferException(String.format(
                "Requested transfer currency [%s] must match source account's currency [%s]",
                requestCurrency.getCurrencyCode(), sourceCurrency.getCurrencyCode()
            ));
        }

        // Convert amount if target has different currency
        BigDecimal amountInTargetCurrency = requestCurrency.equals(targetCurrency)
            ? amount
            : currencyConversionService.convert(amount, requestCurrency, targetCurrency);

        accountService.withdraw(source, amount);
        accountService.deposit(target, amountInTargetCurrency);

        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            source.getId(),
            target.getId(),
            amount,
            requestCurrency,
            Instant.now()
        );

        transactionService.record(transaction);
    }
}
