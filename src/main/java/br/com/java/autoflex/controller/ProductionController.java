package br.com.java.autoflex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.service.ProductionService;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for production management.
 * Provides endpoints to generate production suggestions based on available stock.
 */
@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
public class ProductionController {
    private final ProductionService productionService;

    /**
     * Generates production suggestions for all products based on available raw materials.
     * Uses a greedy algorithm that prioritizes higher-priced products.
     *
     * @return Production suggestion response containing list of items and total value
     */
    @GetMapping("/suggestion")
    public ResponseEntity<ProductionSuggestionResponseDTO> getProductionSuggestion() {
        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();
        return ResponseEntity.ok(response);
    }
}
