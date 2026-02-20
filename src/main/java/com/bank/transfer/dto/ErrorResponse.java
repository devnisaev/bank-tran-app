package com.bank.transfer.dto;

import java.time.Instant;

public record ErrorResponse(
        String errorCode,
        String message,
        Instant timestamp
) {}
