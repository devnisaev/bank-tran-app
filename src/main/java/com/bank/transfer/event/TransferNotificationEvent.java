package com.bank.transfer.event;

import java.util.UUID;

public record TransferNotificationEvent(
        UUID transferId,
        UUID senderId,
        UUID receiverId,
        String message
) {}

