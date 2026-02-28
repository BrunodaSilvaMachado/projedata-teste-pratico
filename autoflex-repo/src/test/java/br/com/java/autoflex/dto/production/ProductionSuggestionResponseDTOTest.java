package br.com.java.autoflex.dto.production;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductionSuggestionResponseDTOTest {

    @Test
    void noArgsConstructorAndSettersGetters() {
        ProductionSuggestionResponseDTO dto = new ProductionSuggestionResponseDTO();

        ProductionItemSuggestionDTO item = new ProductionItemSuggestionDTO(
                1L,
                "Produto A",
                10,
                new BigDecimal("2.5"),
                new BigDecimal("25.0"),
                List.of(
                        new ProductionItemSuggestionDTO.MaterialUsage(1L, "Material X", new BigDecimal("5.0")),
                        new ProductionItemSuggestionDTO.MaterialUsage(2L, "Material Y", new BigDecimal("3.0"))
                )
        );

        List<ProductionItemSuggestionDTO> items = new ArrayList<>();
        items.add(item);
        BigDecimal total = new BigDecimal("25.0");

        dto.setItems(items);
        dto.setTotalValue(total);

        assertSame(items, dto.getItems());
        assertEquals(total, dto.getTotalValue());
    }

    @Test
    void allArgsConstructorAndGetters() {
        List<ProductionItemSuggestionDTO> items = List.of(
                new ProductionItemSuggestionDTO(2L, "Produto B", 5, new BigDecimal("3.0"), new BigDecimal("15.0")
                        , List.of(
                                new ProductionItemSuggestionDTO.MaterialUsage(3L, "Material Z", new BigDecimal("2.0"))
                        )
                )
        );
        BigDecimal total = new BigDecimal("15.0");

        ProductionSuggestionResponseDTO dto = new ProductionSuggestionResponseDTO(items, total);

        assertEquals(items, dto.getItems());
        assertEquals(total, dto.getTotalValue());
    }
}
