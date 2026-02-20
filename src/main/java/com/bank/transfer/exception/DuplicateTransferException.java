package com.bank.transfer.exception;

public class DuplicateTransferException extends BusinessException {

    public DuplicateTransferException() {
        super("DUPLICATE_TRANSFER", "Transfer with this idempotency key already exists");
    }
}

