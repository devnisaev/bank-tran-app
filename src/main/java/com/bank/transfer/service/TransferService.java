package com.bank.transfer.service;

import com.bank.transfer.domain.entity.Account;
import com.bank.transfer.domain.entity.OutboxEvent;
import com.bank.transfer.domain.entity.Transfer;
import com.bank.transfer.domain.enums.TransferStatus;
import com.bank.transfer.domain.repository.AccountRepository;
import com.bank.transfer.domain.repository.OutboxRepository;
import com.bank.transfer.domain.repository.TransferRepository;
import com.bank.transfer.dto.TransferRequest;
import com.bank.transfer.dto.TransferResponse;
import com.bank.transfer.event.TransferCompletedEvent;
import com.bank.transfer.event.TransferNotificationEvent;
import com.bank.transfer.exception.DuplicateTransferException;
import com.bank.transfer.exception.InsufficientFundsException;
import com.bank.transfer.exception.NotFoundException;
import com.bank.transfer.integration.FraudClient;
import com.bank.transfer.mapper.TransferEventMapper;
import com.bank.transfer.mapper.TransferMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final FraudClient fraudWebClient;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "accounts", key = "#request.senderId"),
            @CacheEvict(value = "accounts", key = "#request.receiverId")
    })
    public TransferResponse transfer(TransferRequest request, String idempotencyKey) {

        if (request.getSenderId().equals(request.getReceiverId())) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same");
        }

        // 1 Idempotency check
        transferRepository.findByIdempotencyKey(idempotencyKey)
                .ifPresent(existing -> {
                    throw new DuplicateTransferException();
                });

        // 2 Fraud check
        fraudWebClient.check(request)
                .doOnError(ex -> log.error("Fraud check failed", ex))
                .block();

        // 3 Lock accounts
        Account sender = accountRepository.findByIdForUpdate(request.getSenderId())
                .orElseThrow(() -> new NotFoundException("Sender not found"));

        Account receiver = accountRepository.findByIdForUpdate(request.getReceiverId())
                .orElseThrow(() -> new NotFoundException("Receiver not found"));

        // 4 Check sufficient balance
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException();
        }

        //  5 Update balances
        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        //  6 Save transfer
        Transfer transfer = Transfer.builder()
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .amount(request.getAmount())
                .status(TransferStatus.COMPLETED)
                .idempotencyKey(idempotencyKey)
                .createdAt(Instant.now())
                .build();

        transferRepository.save(transfer);

        // 7 Serialize entity safely
        TransferCompletedEvent eventDto = TransferEventMapper.INSTANCE.toEvent(transfer);
        String payload = toJson(eventDto);

        // 8 Save OutboxEvent
        OutboxEvent event = OutboxEvent.builder()
                .aggregateType("TRANSFER")
                .aggregateId(transfer.getId())
                .eventType("TRANSFER_COMPLETED")
                .payload(payload)
                .processed(false)
                .createdAt(Instant.now())
                .build();

        outboxRepository.save(event);

        // 9 send notification
        sendNotification(transfer);


        // 10 Return response
        return TransferMapper.INSTANCE.toDto(transfer);
    }

    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize object", e);
        }
    }

    private void sendNotification(Transfer transfer) {
        String message = String.format(
                "Transfer of $%.2f from %s to %s completed.",
                transfer.getAmount(),
                transfer.getSenderId(),
                transfer.getReceiverId()
        );

        notificationService.notifyTransferCompleted(new
                TransferNotificationEvent(transfer.getId(), transfer.getSenderId(),
                transfer.getReceiverId(), message)
        );
    }
}


