package io.github.echopay.application.listeners;

import io.github.echopay.application.services.TransactionService;
import io.github.echopay.domain.enums.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionStatusUpdateListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionStatusUpdateListener.class);
    private final TransactionService transactionService;

    public TransactionStatusUpdateListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "transaction-status-updated", groupId = "echopay-group")
    public void handleTransactionStatusUpdate(String transactionUuid) {
        logger.info("Received transaction update for UUID: {}", transactionUuid);

        UUID uuid = UUID.fromString(transactionUuid);
        transactionService.updateTransactionStatus(uuid, TransactionStatus.FINISHED);

        logger.info("Transaction {} status updated to FINISHED", transactionUuid);
    }
}
