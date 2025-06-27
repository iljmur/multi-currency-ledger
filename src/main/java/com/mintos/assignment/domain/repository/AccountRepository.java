package com.mintos.assignment.domain.repository;

import com.mintos.assignment.domain.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(UUID id);
    List<Account> findByClientId(UUID clientId);
    void save(Account account);
}
