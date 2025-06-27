package com.mintos.assignment.api;

import com.mintos.assignment.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Transfer", description = "Transfer operations between accounts")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @Operation(summary = "Transfer funds between accounts with currency conversion support")
    public ResponseEntity<Void> transfer(
        @Valid @RequestBody TransferRequest request
    ) {
        transferService.transfer(
            request.fromAccountId(),
            request.toAccountId(),
            request.amount(),
            Currency.getInstance(request.currency())
        );
        return ResponseEntity.ok().build();
    }

    @Schema(description = "Payload for transfer request")
    public record TransferRequest(
        @Parameter(description = "UUID of the source account", required = true)
        @NotNull UUID fromAccountId,

        @Parameter(description = "UUID of the destination account", required = true)
        @NotNull UUID toAccountId,

        @Parameter(description = "Amount to transfer", required = true, example = "100.00")
        @NotNull @DecimalMin("0.01") BigDecimal amount,

        @Parameter(description = "Currency code (e.g., EUR, USD, CHF)", required = true)
        @NotNull String currency
    ) {}
}
