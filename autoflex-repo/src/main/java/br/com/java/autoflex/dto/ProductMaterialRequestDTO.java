package br.com.java.autoflex.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductMaterialRequestDTO {
    @NotNull
    private Long rawMaterialId;

    @NotNull
    @Positive
    private BigDecimal quantityRequired;
}
