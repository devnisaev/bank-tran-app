package com.bank.transfer.dto;

import com.bank.transfer.domain.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferResponse(
        UUID transferId,
        UUID senderId,
        UUID receiverId,
        BigDecimal amount,
        TransferStatus status,
        Instant createdAt
) {}


