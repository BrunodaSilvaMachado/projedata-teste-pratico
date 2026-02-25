package br.com.java.autoflex.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.dto.production.ProductionItemSuggestionDTO;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.service.ProductionService;

public class ProductionControllerTest {
    private ProductionService productionService;
    private ProductionController productionController;

    @BeforeEach
    void setUp() {
        productionService = mock(ProductionService.class);
        productionController = new ProductionController(productionService);
    }

    @Test
    void shouldReturnProductionSuggestion() {
        ProductionItemSuggestionDTO item1 = new ProductionItemSuggestionDTO(1L, "Car", 10, new BigDecimal(50000), new BigDecimal("500000"));
        ProductionItemSuggestionDTO item2 = new ProductionItemSuggestionDTO(2L, "Bike", 20, new BigDecimal(1000), new BigDecimal("20000"));
        ProductionSuggestionResponseDTO responseDTO = new ProductionSuggestionResponseDTO(List.of(item1, item2), new BigDecimal("520000"));
        when(productionService.generateProductionSuggestionResponseDTO()).thenReturn(responseDTO);

        ProductionSuggestionResponseDTO result = productionController.getProductionSuggestion().getBody();
        assertNotNull(result);
        assertEquals(responseDTO, result);
        assertTrue(result.getItems().size() > 0);
    }
}
