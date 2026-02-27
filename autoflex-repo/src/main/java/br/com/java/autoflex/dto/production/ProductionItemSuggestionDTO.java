package br.com.java.autoflex.dto.production;

import jakarta.validation.constraints.*; //remover
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductionItemSuggestionDTO {
    @NotNull
    private Long productId;
    @NotBlank
    private String productName;
    @NotNull
    @Positive
    private Integer quantityToProduce;
    @NotNull
    @Positive
    private BigDecimal unitPrice;
    @NotNull
    private BigDecimal totalValue;
}
