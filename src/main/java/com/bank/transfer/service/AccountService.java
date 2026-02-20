package com.bank.transfer.service;

import com.bank.transfer.domain.repository.AccountRepository;
import com.bank.transfer.dto.AccountResponse;
import com.bank.transfer.mapper.AccountMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Cacheable(value = "accounts", key = "#clientId")
    public List<AccountResponse> getAccounts(UUID clientId) {
        return AccountMapper.INSTANCE.toDtoList(
                accountRepository.findByClientId(clientId)
        );
    }
}
