package com.bank.transfer.exception;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message);
    }
}

