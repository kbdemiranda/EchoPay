package io.github.echopay.application.dtos.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.echopay.domain.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionInputDTO {

    @JsonProperty("amount")
    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount must be greater than or equal to zero")
    private BigDecimal amount;

    @JsonProperty("payment_method")
    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    @JsonProperty("currency_code")
    @NotBlank(message = "Currency code cannot be blank")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currencyCode;

    @JsonProperty("description")
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
