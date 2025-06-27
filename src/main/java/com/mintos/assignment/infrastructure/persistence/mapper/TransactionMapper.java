package com.mintos.assignment.infrastructure.persistence.mapper;

import com.mintos.assignment.domain.model.Transaction;
import com.mintos.assignment.infrastructure.persistence.TransactionEntity;

import java.util.Currency;

public class TransactionMapper {

    public static Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
            entity.getId(),
            entity.getSourceAccountId(),
            entity.getTargetAccountId(),
            entity.getAmount(),
            Currency.getInstance(entity.getCurrency()),
            entity.getTimestamp()
        );
    }

    public static TransactionEntity toEntity(Transaction transaction) {
        return new TransactionEntity(
            transaction.getId(),
            transaction.getSourceAccountId(),
            transaction.getTargetAccountId(),
            transaction.getAmount(),
            transaction.getCurrency().getCurrencyCode(),
            transaction.getTimestamp()
        );
    }
}
