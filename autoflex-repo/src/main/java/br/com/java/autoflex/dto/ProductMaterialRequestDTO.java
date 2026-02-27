package br.com.java.autoflex.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
// DTO to receive the request for associating a raw material with a product. It includes validation annotations to ensure that the required fields are provided and valid.
@Data
public class ProductMaterialRequestDTO {
    @NotNull
    private Long rawMaterialId;

    @NotNull
    @Positive
    private BigDecimal quantityRequired;
}
