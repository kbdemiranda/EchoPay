package io.github.echopay.application.dtos.outputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.echopay.domain.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionOutputDTO {

    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
    @JsonProperty("currency_code")
    private String currencyCode;
    @JsonProperty("description")
    private String description;
    @JsonProperty("status")
    private String status;
    @JsonProperty("created_at")
    private String createdAt;
}

