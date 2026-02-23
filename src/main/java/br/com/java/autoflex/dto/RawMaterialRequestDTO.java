package br.com.java.autoflex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

// DTO to receive the request for creating a new raw material. It includes validation annotations to ensure that the required fields are provided and valid.
@Data
public class RawMaterialRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "code is required")
    private String code;

    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be positive")
    private Double stockQuantity;

    @NotNull(message = "unit is required")
    private String unit;
}
