package br.com.java.autoflex.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RawMaterialResponseDTO {
    private Long id;
    private String name;
    private String code;
    private BigDecimal stockQuantity;
    private String unit;
}
