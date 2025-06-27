package com.mintos.assignment.infrastructure.persistence;

import com.mintos.assignment.infrastructure.persistence.TransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findBySourceAccountIdOrTargetAccountIdOrderByTimestampDesc(
        UUID sourceAccountId, UUID targetAccountId, Pageable pageable
    );
}
