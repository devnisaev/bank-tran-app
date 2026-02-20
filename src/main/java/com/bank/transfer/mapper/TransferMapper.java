package com.bank.transfer.mapper;

import com.bank.transfer.domain.entity.Transfer;
import com.bank.transfer.dto.TransferResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransferMapper {
    TransferMapper INSTANCE = Mappers.getMapper(TransferMapper.class);

    @Mapping(source = "id", target = "transferId")
    TransferResponse toDto(Transfer transfer);

    List<TransferResponse> toDtoList(List<Transfer> transfers);
}

