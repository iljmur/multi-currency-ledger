package com.mintos.assignment.api;

import com.mintos.assignment.domain.model.Client;
import com.mintos.assignment.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client", description = "Operations related to clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a client by their UUID",
        parameters = {
            @Parameter(
                name = "id",
                description = "UUID of the client to retrieve",
                required = true
            )
        })
    public Client getClient(
        @PathVariable UUID id
    ) {
        return clientService.getClient(id);
    }
}
