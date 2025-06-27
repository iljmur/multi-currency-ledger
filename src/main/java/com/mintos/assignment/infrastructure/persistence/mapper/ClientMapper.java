package com.mintos.assignment.infrastructure.persistence.mapper;

import com.mintos.assignment.domain.model.Client;
import com.mintos.assignment.infrastructure.persistence.ClientEntity;

public class ClientMapper {

    public static Client toDomain(ClientEntity entity) {
        return new Client(
            entity.getId(),
            entity.getName(),
            entity.getEmail()
        );
    }

    public static ClientEntity toEntity(Client client) {
        return new ClientEntity(
            client.getId(),
            client.getName(),
            client.getEmail()
        );
    }
}
