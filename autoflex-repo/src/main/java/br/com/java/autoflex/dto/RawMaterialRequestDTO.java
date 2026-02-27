package br.com.java.autoflex.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RawMaterialRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "code is required")
    private String code;

    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be positive")
    private BigDecimal stockQuantity;

    @NotNull(message = "unit is required")
    private String unit;
}
