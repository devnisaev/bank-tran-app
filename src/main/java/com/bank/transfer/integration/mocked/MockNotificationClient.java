package com.bank.transfer.integration.mocked;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockNotificationClient {

    /**
     * Simulates sending transfer notification to an external service.
     * Could add delay, throw exceptions, etc.
     */
    public void send(String message) {
        try {
            // Simulate network delay
            Thread.sleep(100); // 100ms delay
            // Simulate sending
            log.info("📤 [MOCK SERVICE] Sent notification: {}", message);

            // Optional: simulate occasional failure
            // if (Math.random() < 0.1) throw new RuntimeException("Mock failure");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Notification send interrupted", e);
        }
    }
}
