package br.com.java.autoflex.dto.production;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

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
    @NotNull
    private List<MaterialUsage> materials;

    @Getter
    public static class MaterialUsage {
        private Long rawMaterialId;
        private String rawMaterialName;
        private BigDecimal quantityUsed;

        public MaterialUsage(Long rawMaterialId, String rawMaterialName, BigDecimal quantityUsed) {
            this.rawMaterialId = rawMaterialId;
            this.rawMaterialName = rawMaterialName;
            this.quantityUsed = quantityUsed;
        }
    }
}
