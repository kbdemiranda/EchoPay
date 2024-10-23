package io.github.echopay.application.services;

import io.github.echopay.application.dtos.inputs.TransactionInputDTO;
import io.github.echopay.application.dtos.outputs.TransactionOutputDTO;
import io.github.echopay.domain.enums.TransactionStatus;

import java.util.Optional;
import java.util.UUID;

public interface TransactionService {

    UUID createTransaction(TransactionInputDTO dto);
    TransactionOutputDTO getTransactionByUuid(UUID uuid);

}
