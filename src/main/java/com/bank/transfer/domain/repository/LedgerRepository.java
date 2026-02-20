package com.bank.transfer.domain.repository;

import com.bank.transfer.domain.entity.LedgerEntry;
import com.bank.transfer.domain.enums.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LedgerRepository extends JpaRepository<LedgerEntry, UUID> {

    boolean existsByTransferIdAndAccountIdAndDirection(
            UUID transferId,
            UUID accountId,
            Direction direction
    );
}

