package com.bank.transfer.service;

import com.bank.transfer.event.TransferNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.transfer-notifications}")
    private String transferNotificationsTopic;

    public void notifyTransferCompleted(TransferNotificationEvent event) {
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(transferNotificationsTopic, event.transferId().toString(), event);

        future.thenAccept(result ->
                log.info("Notification sent for transfer {} to topic {}",
                        event.transferId(), result.getRecordMetadata().topic())
        ).exceptionally(ex -> {
            log.error("Failed to send notification for transfer {}", event.transferId(), ex);
            return null;
        });
    }
}
