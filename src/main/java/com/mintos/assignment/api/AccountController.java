package com.mintos.assignment.api;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.service.AccountService;
import com.mintos.assignment.service.TransactionService;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/client/{clientId}")
    public List<Account> getAccountsByClient(@PathVariable UUID clientId) {
        return accountService.getAccountsByClientId(clientId);
    }

    @GetMapping("/{accountId}/transactions")
    public List<Transaction> getTransactionHistory(
        @PathVariable UUID accountId,
        @RequestParam(defaultValue = "0") @Min(0) int offset,
        @RequestParam(defaultValue = "20") @Min(1) int limit
    ) {
        return transactionService.getTransactions(accountId, offset, limit);
    }
}
