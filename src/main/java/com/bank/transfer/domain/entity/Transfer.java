package com.bank.transfer.domain.entity;

import com.bank.transfer.domain.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID senderId;
    private UUID receiverId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private String idempotencyKey;

    private Instant createdAt;
}
