package com.bank.transfer.mapper;

import com.bank.transfer.domain.entity.Account;
import com.bank.transfer.dto.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountResponse toDto(Account account);

    List<AccountResponse> toDtoList(List<Account> accounts);
}

