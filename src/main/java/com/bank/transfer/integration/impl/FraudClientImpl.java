package com.bank.transfer.integration.impl;

import com.bank.transfer.dto.TransferRequest;
import com.bank.transfer.exception.FraudDetectedException;
import com.bank.transfer.exception.FraudServiceUnavailableException;
import com.bank.transfer.integration.FraudClient;
import com.bank.transfer.integration.dto.FraudResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class FraudClientImpl implements FraudClient {

    private final WebClient fraudWebClient;

    public FraudClientImpl(@Qualifier("fraudWebClient") WebClient fraudWebClient) {
        this.fraudWebClient = fraudWebClient;
    }

    @Override
    @Retry(name = "fraudRetry")
    @CircuitBreaker(name = "fraudCircuit", fallbackMethod = "fallback")
    @Bulkhead(name = "fraudBulkhead", type = Bulkhead.Type.SEMAPHORE)
    public Mono<Void> check(TransferRequest request) {
        return fraudWebClient.post()
                .uri("/fraud/check")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FraudResponse.class)
                .timeout(Duration.ofMillis(200)) // fast sync call
                .flatMap(response -> {
                    if (response.approved()) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new FraudDetectedException("Fraud detected"));
                    }
                });
    }

    private Mono<Void> fallback(TransferRequest request, Throwable ex) {
        return Mono.error(new FraudServiceUnavailableException("Fraud service unavailable", ex));
    }
}

