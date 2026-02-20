package com.bank.transfer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String aggregateType; // TRANSFER

    @Column(nullable = false)
    private UUID aggregateId; // transferId

    @Column(nullable = false)
    private String eventType; // TRANSFER_COMPLETED

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload; // JSON

    @Column(nullable = false)
    private boolean processed;

    private Instant createdAt;
}
