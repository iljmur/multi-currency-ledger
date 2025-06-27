package com.mintos.assignment.infrastructure.persistence;

import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.domain.repository.TransactionRepository;
import com.mintos.assignment.infrastructure.persistence.mapper.TransactionMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JpaTransactionRepository jpaRepo;

    public TransactionRepositoryImpl(JpaTransactionRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void save(Transaction transaction) {
        jpaRepo.save(TransactionMapper.toEntity(transaction));
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId, int offset, int limit) {
        return jpaRepo.findBySourceAccountIdOrTargetAccountIdOrderByTimestampDesc(
                accountId, accountId, PageRequest.of(offset / limit, limit)
            ).stream()
            .map(TransactionMapper::toDomain)
            .collect(Collectors.toList());
    }
}
