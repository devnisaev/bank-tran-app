package com.bank.transfer.integration.dto;

public record FraudResponse(
        boolean approved,
        String reason
) {}
