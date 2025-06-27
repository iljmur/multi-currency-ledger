package com.mintos.assignment.domain.repository;

import com.mintos.assignment.domain.model.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    Optional<Client> findById(UUID id);
    void save(Client client);
}
