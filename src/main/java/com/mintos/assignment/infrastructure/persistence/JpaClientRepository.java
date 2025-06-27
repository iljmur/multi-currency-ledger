package com.mintos.assignment.infrastructure.persistence;

import com.mintos.assignment.infrastructure.persistence.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaClientRepository extends JpaRepository<ClientEntity, UUID> {
}