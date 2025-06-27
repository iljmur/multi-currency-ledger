package com.mintos.assignment.api;

import com.mintos.assignment.domain.model.Client;
import com.mintos.assignment.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable UUID id) {
        return clientService.getClient(id);
    }
}
