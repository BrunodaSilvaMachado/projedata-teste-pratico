package br.com.java.autoflex.dto;

import lombok.Builder;
import lombok.Data;
// DTO to send the response for raw material operations. It can be extended in the future to include additional fields as needed.
@Data
@Builder
public class RawMaterialResponseDTO {
    private Long id;
    private String name;
    private String code;
    private Double stockQuantity;
    private String unit;
}
