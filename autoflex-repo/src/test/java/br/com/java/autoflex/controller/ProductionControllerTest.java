package br.com.java.autoflex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.java.autoflex.dto.production.ProductionItemSuggestionDTO;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.service.ProductionService;

/**
 * Unit tests for ProductionController.
 * Tests REST endpoints for production suggestion retrieval.
 */
class ProductionControllerTest {
    private ProductionService productionService;
    private ProductionController productionController;

    @BeforeEach
    void setUp() {
        productionService = mock(ProductionService.class);
        productionController = new ProductionController(productionService);
    }

    @Test
    void testReturnProductionSuggestionSuccessfully() {
        ProductionItemSuggestionDTO carProduction = new ProductionItemSuggestionDTO(
                1L, "Car", 10, new BigDecimal("5000"), new BigDecimal("50000"), List.of(
                        new ProductionItemSuggestionDTO.MaterialUsage(1L, "Steel", new BigDecimal("100")),
                        new ProductionItemSuggestionDTO.MaterialUsage(2L, "Plastic", new BigDecimal("50"))
        ));
        ProductionItemSuggestionDTO bikeProduction = new ProductionItemSuggestionDTO(
                2L, "Bike", 20, new BigDecimal("1000"), new BigDecimal("20000"), List.of(
                        new ProductionItemSuggestionDTO.MaterialUsage(1L, "Steel", new BigDecimal("50")),
                        new ProductionItemSuggestionDTO.MaterialUsage(3L, "Rubber", new BigDecimal("30"))
        ));

        ProductionSuggestionResponseDTO responseDTO = new ProductionSuggestionResponseDTO(
                List.of(carProduction, bikeProduction), new BigDecimal("70000"));

        when(productionService.generateProductionSuggestionResponseDTO()).thenReturn(responseDTO);

        ProductionSuggestionResponseDTO result = productionController.getProductionSuggestion().getBody();

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals(new BigDecimal("70000"), result.getTotalValue());
    }
}
