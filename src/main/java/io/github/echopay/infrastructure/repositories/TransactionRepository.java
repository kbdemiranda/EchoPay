package io.github.echopay.infrastructure.repositories;

import io.github.echopay.domain.enums.TransactionStatus;
import io.github.echopay.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByStatus(TransactionStatus status);
    boolean existsByUuid(UUID uuid);

}
