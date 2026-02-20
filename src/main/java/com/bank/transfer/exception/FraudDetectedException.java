package com.bank.transfer.exception;

import lombok.Getter;

@Getter
public class FraudDetectedException extends BusinessException {

    public FraudDetectedException(String message) {
        super("FRAUD_DETECTED", message);
    }
}

