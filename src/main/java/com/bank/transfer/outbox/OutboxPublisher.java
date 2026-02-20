package com.bank.transfer.outbox;

import com.bank.transfer.domain.entity.OutboxEvent;
import com.bank.transfer.domain.repository.OutboxRepository;
import com.bank.transfer.event.TransferCompletedEvent;
import com.bank.transfer.ledger.LedgerEventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final LedgerEventHandler ledgerEventHandler;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelayString = "${outbox.publish.delay:500}")
    @Transactional
    public void publishEvents() {
        List<OutboxEvent> events = outboxRepository.findUnprocessedEvents(
                PageRequest.of(0, 50, Sort.by("createdAt").ascending())
        );

        if (events.isEmpty()) {
            return;
        }

        log.info("Publishing {} outbox events", events.size());

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                        "transfer.completed",
                        event.getAggregateId().toString(),
                        event.getPayload()
                ).get();

                event.setProcessed(true);

                log.debug("Successfully published event {}", event.getId());

                TransferCompletedEvent completedEvent = objectMapper.readValue(event.getPayload(), TransferCompletedEvent.class);

                ledgerEventHandler.consume(completedEvent);

            } catch (Exception e) {
                log.error("Failed to publish event {}", event.getId(), e);
                break;
            }
        }
    }
}

