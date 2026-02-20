package com.bank.transfer.controller;

import com.bank.transfer.dto.TransferRequest;
import com.bank.transfer.dto.TransferResponse;
import com.bank.transfer.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/send")
    public ResponseEntity<TransferResponse> transfer(
            @RequestBody @Valid TransferRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return ResponseEntity.ok(
                transferService.transfer(request, idempotencyKey)
        );
    }
}

