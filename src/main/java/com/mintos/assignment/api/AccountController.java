package com.mintos.assignment.api;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.service.AccountService;
import com.mintos.assignment.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account", description = "Operations related to accounts and transactions")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "List all accounts for a given client",
        parameters = {
            @Parameter(
                name = "clientId",
                description = "UUID of the client",
                required = true
            )
        })
    public List<Account> getAccountsByClient(
        @PathVariable UUID clientId
    ) {
        return accountService.getAccountsByClientId(clientId);
    }

    @GetMapping("/{accountId}/transactions")
    @Operation(
        summary = "Retrieve transaction history for an account (paginated)",
        parameters = {
            @Parameter(name = "accountId", description = "UUID of the account", required = true),
            @Parameter(name = "offset", description = "Offset for paging", example = "0"),
            @Parameter(name = "limit", description = "Page size limit", example = "20")
        }
    )
    public List<Transaction> getTransactionHistory(
        @PathVariable UUID accountId,
        @RequestParam(defaultValue = "0") @Min(0) int offset,
        @RequestParam(defaultValue = "20") @Min(1) int limit
    ) {
        return transactionService.getTransactions(accountId, offset, limit);
    }
}
