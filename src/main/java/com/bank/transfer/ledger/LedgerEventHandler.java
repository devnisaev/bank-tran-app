package com.bank.transfer.ledger;

import com.bank.transfer.event.TransferCompletedEvent;
import com.bank.transfer.service.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LedgerEventHandler {

    private final LedgerService ledgerService;

    public void consume(TransferCompletedEvent event) {
        ledgerService.handleTransferCompleted(event);
    }
}
