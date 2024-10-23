package io.github.echopay.infrastructure.exceptions;

import java.util.UUID;

public class TransactionNotFoundException extends RuntimeException {
        public TransactionNotFoundException(UUID uuid) {
            super("Transaction not found with UUID: " + uuid);
        }
}
