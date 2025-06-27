package com.mintos.assignment.infrastructure.persistence.mapper;

import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.infrastructure.persistence.AccountEntity;

import java.util.Currency;

public class AccountMapper {

    public static Account toDomain(AccountEntity entity) {
        return new Account(
            entity.getId(),
            entity.getClientId(),
            Currency.getInstance(entity.getCurrency()),
            entity.getBalance()
        );
    }

    public static AccountEntity toEntity(Account account) {
        return new AccountEntity(
            account.getId(),
            account.getClientId(),
            account.getCurrency().getCurrencyCode(),
            account.getBalance()
        );
    }
}
