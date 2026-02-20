package com.bank.transfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @NotNull
    private UUID senderId;

    @NotNull
    private UUID receiverId;

    @NotNull
    @Positive
    @DecimalMin("0.01")
    private BigDecimal amount;
}

