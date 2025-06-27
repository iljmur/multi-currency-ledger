package com.mintos.assignment.infrastructure.persistence;

import com.mintos.assignment.domain.model.Client;
import com.mintos.assignment.domain.repository.ClientRepository;
import com.mintos.assignment.infrastructure.persistence.mapper.ClientMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final JpaClientRepository jpaRepo;

    public ClientRepositoryImpl(JpaClientRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return jpaRepo.findById(id).map(ClientMapper::toDomain);
    }

    @Override
    public void save(Client client) {
        jpaRepo.save(ClientMapper.toEntity(client));
    }
}
