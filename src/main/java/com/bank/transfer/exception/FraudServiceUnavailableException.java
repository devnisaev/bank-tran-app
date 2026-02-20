package com.bank.transfer.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class FraudServiceUnavailableException extends BusinessException {

    public FraudServiceUnavailableException(String message,  Throwable ex) {
        super("FRAUD_SERVICE", message);
        log.error(ex.getMessage(), ex);
    }
}
