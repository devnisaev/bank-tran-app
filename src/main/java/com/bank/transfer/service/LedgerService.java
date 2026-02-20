package com.bank.transfer.service;

import com.bank.transfer.domain.entity.LedgerEntry;
import com.bank.transfer.domain.enums.Direction;
import com.bank.transfer.domain.repository.LedgerRepository;
import com.bank.transfer.event.TransferCompletedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    @Transactional
    public void handleTransferCompleted(TransferCompletedEvent event) {

        createIfNotExists(
                event.transferId(),
                event.senderId(),
                Direction.DEBIT,
                event.amount(),
                event.createdAt()
        );

        createIfNotExists(
                event.transferId(),
                event.receiverId(),
                Direction.CREDIT,
                event.amount(),
                event.createdAt()
        );
    }

    private void createIfNotExists(
            UUID transferId,
            UUID accountId,
            Direction direction,
            BigDecimal amount,
            Instant createdAt
    ) {

        boolean exists = ledgerRepository
                .existsByTransferIdAndAccountIdAndDirection(
                        transferId,
                        accountId,
                        direction
                );

        if (exists) return;

        LedgerEntry entry = LedgerEntry.builder()
                .transferId(transferId)
                .accountId(accountId)
                .direction(direction)
                .amount(amount)
                .createdAt(createdAt)
                .build();

        ledgerRepository.save(entry);
    }
}

