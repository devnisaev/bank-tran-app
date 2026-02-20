package com.bank.transfer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient fraudWebClient() {

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(200));

        return WebClient.builder()
                .baseUrl("http://localhost:8080") // points to mock controller
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
