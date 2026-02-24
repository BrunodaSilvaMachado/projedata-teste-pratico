package br.com.java.autoflex.dto.production;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProductionSuggestionResponseDTO {
    private List<ProductionItemSuggestionDTO> items;
    private BigDecimal totalValue;

    public List<ProductionItemSuggestionDTO> getItems() {
        return items;
    }

    public void setItems(List<ProductionItemSuggestionDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}
