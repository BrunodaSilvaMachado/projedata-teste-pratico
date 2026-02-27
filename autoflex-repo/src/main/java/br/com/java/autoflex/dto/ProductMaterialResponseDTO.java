package br.com.java.autoflex.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductMaterialResponseDTO {

    private Long rawMaterialId;
    private String rawMaterialName;
    private Double quantityRequired;
}