package com.mintos.assignment.infrastructure.persistence;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.repository.AccountRepository;
import com.mintos.assignment.infrastructure.persistence.mapper.AccountMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaAccountRepository jpaRepo;

    public AccountRepositoryImpl(JpaAccountRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaRepo.findById(id).map(AccountMapper::toDomain);
    }

    @Override
    public List<Account> findByClientId(UUID clientId) {
        return jpaRepo.findByClientId(clientId).stream()
            .map(AccountMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void save(Account account) {
        jpaRepo.save(AccountMapper.toEntity(account));
    }
}
