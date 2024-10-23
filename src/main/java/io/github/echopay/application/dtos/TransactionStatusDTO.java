package io.github.echopay.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionStatusDTO {
    @JsonProperty("status")
    @NotNull
    private String status;
}
