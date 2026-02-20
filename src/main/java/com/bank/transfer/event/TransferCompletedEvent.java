package com.bank.transfer.event;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record TransferCompletedEvent(
        UUID transferId,
        UUID senderId,
        UUID receiverId,
        BigDecimal amount,
        Instant createdAt
) {
}

