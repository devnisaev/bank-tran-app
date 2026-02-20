package com.bank.transfer.exception;

public class InsufficientFundsException extends BusinessException {

    public InsufficientFundsException() {
        super("INSUFFICIENT_FUNDS", "Sender does not have enough balance");
    }
}


