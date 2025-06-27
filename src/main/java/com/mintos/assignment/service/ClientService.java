package com.mintos.assignment.service;

import com.mintos.assignment.domain.model.Client;
import com.mintos.assignment.domain.repository.ClientRepository;
import com.mintos.assignment.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClient(UUID clientId) {
        return clientRepository.findById(clientId)
            .orElseThrow(() -> new ApiException("Client not found: " + clientId));
    }
}
