package io.github.echopay.domain.models;

import io.github.echopay.domain.enums.PaymentMethod;
import io.github.echopay.domain.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions", schema = "echopay")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    private UUID uuid;

    @Column(name="amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false, length = 10)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
