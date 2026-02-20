package com.bank.transfer.controller;

import com.bank.transfer.dto.AccountResponse;
import com.bank.transfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{clientId}")
    public List<AccountResponse> getAccounts(@PathVariable UUID clientId) {
        return accountService.getAccounts(clientId);
    }
}

