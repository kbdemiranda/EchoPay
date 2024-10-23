package io.github.echopay.application.controllers;

import io.github.echopay.application.dtos.TransactionStatusDTO;
import io.github.echopay.application.dtos.inputs.TransactionInputDTO;
import io.github.echopay.application.dtos.outputs.TransactionOutputDTO;
import io.github.echopay.application.services.TransactionService;
import io.github.echopay.domain.enums.TransactionStatus;
import jakarta.validation.Valid;
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

    @PutMapping("/{uuid}/status")
    public ResponseEntity<Void> updateTransactionStatus(@PathVariable UUID uuid, @RequestBody @Valid TransactionStatusDTO status) {
        logger.info("Received request to update status of transaction with UUID: {} to status: {}", uuid, status);
        try {
            transactionService.updateTransactionStatus(uuid, Enum.valueOf(TransactionStatus.class, status.getStatus().toUpperCase()));
            logger.info("Transaction status updated successfully for UUID: {}", uuid);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status provided: {}", status.getStatus(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            logger.error("Failed to update status for transaction with UUID: {}", uuid, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
