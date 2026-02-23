package br.com.java.autoflex.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponseDTO {

    private Long id;
    private String code;
    private String name;
    private Double price;
    private List<ProductMaterialResponseDTO> materials;
}