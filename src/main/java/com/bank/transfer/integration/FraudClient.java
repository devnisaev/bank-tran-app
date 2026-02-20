package com.bank.transfer.integration;

import com.bank.transfer.dto.TransferRequest;
import reactor.core.publisher.Mono;

public interface FraudClient {

    Mono<Void> check(TransferRequest request);
}
