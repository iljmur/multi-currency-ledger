package com.mintos.assignment.api;

import com.mintos.assignment.service.TransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        transferService.transfer(
            request.fromAccountId(),
            request.toAccountId(),
            request.amount(),
            Currency.getInstance(request.currency())
        );
        return ResponseEntity.ok().build();
    }

    public record TransferRequest(
        @NotNull UUID fromAccountId,
        @NotNull UUID toAccountId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull String currency
    ) {}
}
