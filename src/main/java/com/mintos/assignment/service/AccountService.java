package com.mintos.assignment.service;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.repository.AccountRepository;
import com.mintos.assignment.exception.AccountNotFoundException;
import com.mintos.assignment.exception.ApiException;
import com.mintos.assignment.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

    public List<Account> getAccountsByClientId(UUID clientId) {
        return accountRepository.findByClientId(clientId);
    }

    public void withdraw(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        account.withdraw(amount);
        accountRepository.save(account);
    }

    public void deposit(Account account, BigDecimal amount) {
        account.deposit(amount);
        accountRepository.save(account);
    }
}
