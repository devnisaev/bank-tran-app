package com.bank.transfer.mapper;

import com.bank.transfer.domain.entity.Transfer;
import com.bank.transfer.event.TransferCompletedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransferEventMapper {

    TransferEventMapper INSTANCE = Mappers.getMapper(TransferEventMapper.class);

    @Mapping(source = "id", target = "transferId")
    TransferCompletedEvent toEvent(Transfer transfer);
}
