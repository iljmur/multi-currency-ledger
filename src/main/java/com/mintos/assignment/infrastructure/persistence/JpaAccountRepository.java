package com.mintos.assignment.infrastructure.persistence;

import com.mintos.assignment.infrastructure.persistence.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaAccountRepository extends JpaRepository<AccountEntity, UUID> {
    List<AccountEntity> findByClientId(UUID clientId);
}