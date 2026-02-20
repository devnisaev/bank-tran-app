package com.bank.transfer.integration.mocked;

import com.bank.transfer.event.TransferNotificationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MockEmailConsumer {

    private final MockNotificationClient mockNotificationClient;

    @KafkaListener(
            topics = "${spring.kafka.topics.transfer-notifications}",
            groupId = "notification-group"
    )
    public void consume(TransferNotificationEvent event) {
        // Simulate sending email
        log.info("📧 [MOCK] Sending email with details: Transfer {} with message ${} completed.",
                event.transferId(), event.message());

        // Call the "external service"
        mockNotificationClient.send(event.message());
    }
}
