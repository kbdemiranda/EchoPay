package io.github.echopay.application.controllers;

import io.github.echopay.application.dtos.inputs.TransactionInputDTO;
import io.github.echopay.application.dtos.outputs.TransactionOutputDTO;
import io.github.echopay.application.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<UUID> createTransaction(@RequestBody TransactionInputDTO dto) {
        logger.info("Received request to create transaction with data: {}", dto);
        UUID transactionId = transactionService.createTransaction(dto);
        logger.info("Transaction created successfully with UUID: {}", transactionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionId);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TransactionOutputDTO> getTransaction(@PathVariable UUID uuid) {
        return ResponseEntity.ok(transactionService.getTransactionByUuid(uuid));
    }
}
