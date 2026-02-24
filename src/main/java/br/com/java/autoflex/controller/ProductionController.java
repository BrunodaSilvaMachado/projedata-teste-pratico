package br.com.java.autoflex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.service.ProductionService;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/production")
@AllArgsConstructor
public class ProductionController {
    private final ProductionService productionService;

    @GetMapping("/suggestion")
    public ResponseEntity<ProductionSuggestionResponseDTO> getProductionSuggestion() {
        ProductionSuggestionResponseDTO response = productionService.generateProductionSuggestionResponseDTO();
        return ResponseEntity.ok(response);
    }
}
