package br.com.java.autoflex.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.repository.ProductRepository;
import br.com.java.autoflex.repository.RawMaterialRepository;
import br.com.java.autoflex.service.strategy.ProductionAlgorithm;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for generating production suggestions based on available stock.
 * Delegates the suggestion algorithm to a pluggable {@link ProductionAlgorithm} strategy.
 */
@Service
@RequiredArgsConstructor
public class ProductionServiceImpl implements ProductionService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductionAlgorithm productionAlgorithm;

    @Override
    public ProductionSuggestionResponseDTO generateProductionSuggestionResponseDTO() {
        List<Product> allProducts = productRepository.findAllWithMaterials();
        Map<Long, BigDecimal> availableStock = loadAvailableStock();
        
        return productionAlgorithm.generateSuggestions(allProducts, availableStock);
    }

    /**
     * Loads current stock quantities for all raw materials.
     */
    private Map<Long, BigDecimal> loadAvailableStock() {
        Map<Long, BigDecimal> stock = new HashMap<>();
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        for (RawMaterial rm : rawMaterials) {
            stock.put(rm.getId(), rm.getStockQuantity());
        }
        return stock;
    }
}
