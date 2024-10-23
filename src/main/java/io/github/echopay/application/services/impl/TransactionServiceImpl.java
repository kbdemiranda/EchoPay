package io.github.echopay.application.services.impl;

import io.github.echopay.application.dtos.inputs.TransactionInputDTO;
import io.github.echopay.application.dtos.outputs.TransactionOutputDTO;
import io.github.echopay.application.services.TransactionService;
import io.github.echopay.domain.enums.TransactionStatus;
import io.github.echopay.domain.models.Transaction;
import io.github.echopay.infrastructure.exceptions.TransactionNotFoundException;
import io.github.echopay.infrastructure.repositories.TransactionRepository;
import io.github.echopay.utils.UUIDGenerator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final UUIDGenerator uuidGenerator;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ModelMapper modelMapper, UUIDGenerator uuidGenerator) {
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    @Transactional
    public UUID createTransaction(TransactionInputDTO dto) {
        logger.info("Starting to create transaction with input: {}", dto);
        UUID uuid = uuidGenerator.generateUUID();
        logger.debug("Generated UUID for transaction: {}", uuid);

        Transaction transaction = modelMapper.map(dto, Transaction.class);
        transaction.setUuid(uuid);
        transaction.setStatus(TransactionStatus.PENDING);
        logger.debug("Mapped transaction: {}", transaction);

        transactionRepository.save(transaction);
        logger.info("Transaction saved with UUID: {}", uuid);

        return transaction.getUuid();
    }

    @Override
    public TransactionOutputDTO getTransactionByUuid(UUID uuid) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(uuid);
        return transactionOptional.map(transaction -> modelMapper.map(transaction, TransactionOutputDTO.class))
                .orElseThrow(() -> new TransactionNotFoundException(uuid));
    }
}
