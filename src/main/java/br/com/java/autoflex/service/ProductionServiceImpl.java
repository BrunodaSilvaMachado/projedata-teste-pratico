package br.com.java.autoflex.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.domain.RawMaterial;
import br.com.java.autoflex.dto.production.ProductionItemSuggestionDTO;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;
import br.com.java.autoflex.repository.ProductRepository;
import br.com.java.autoflex.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for generating production suggestions based on available stock.
 * Uses a greedy algorithm that prioritizes products with higher prices.
 */
@Service
@RequiredArgsConstructor
public class ProductionServiceImpl implements ProductionService {

    private static final int DEFAULT_MIN_PRODUCTION_UNITS = 1;

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    @Override
    public ProductionSuggestionResponseDTO generateProductionSuggestionResponseDTO() {
        List<Product> allProducts = productRepository.findAllWithMaterials();
        Map<Long, BigDecimal> availableStock = loadAvailableStock();
        
        allProducts.sort(Comparator.comparing(Product::getPrice).reversed());

        return generateProductionSuggestions(allProducts, availableStock);
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

    /**
     * Calculates maximum producible units based on available stock.
     * Returns Integer.MAX_VALUE if product has no material constraints.
     */
    private int calculateMaxProducibleUnits(Product product, Map<Long, BigDecimal> availableStock) {
        int maxUnits = Integer.MAX_VALUE;
        
        for (ProductMaterial pm : product.getMaterials()) {
            BigDecimal requiredQuantity = pm.getQuantityRequired();

            if (requiredQuantity.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            BigDecimal stock = availableStock.getOrDefault(pm.getRawMaterial().getId(), BigDecimal.ZERO);
            int possibleUnits = stock.divide(requiredQuantity, 0, RoundingMode.DOWN).intValue();
            maxUnits = Math.min(maxUnits, possibleUnits);
        }
        
        return maxUnits;
    }

    /**
     * Generates production item suggestion and updates virtual stock accordingly.
     */
    private ProductionItemSuggestionDTO generateProductionItem(
            Product product, 
            Map<Long, BigDecimal> availableStock, 
            int unitsToProduce) {
        
        BigDecimal totalValue = product.getPrice().multiply(BigDecimal.valueOf(unitsToProduce));
        updateVirtualStock(product, availableStock, unitsToProduce);
        
        return new ProductionItemSuggestionDTO(
                product.getId(),
                product.getName(),
                unitsToProduce,
                product.getPrice(),
                totalValue
        );
    }

    /**
     * Updates virtual stock by subtracting consumed materials.
     */
    private void updateVirtualStock(Product product, Map<Long, BigDecimal> availableStock, int unitsToProduce) {
        for (ProductMaterial pm : product.getMaterials()) {
            Long materialId = pm.getRawMaterial().getId();
            BigDecimal consumedQuantity = pm.getQuantityRequired().multiply(BigDecimal.valueOf(unitsToProduce));
            BigDecimal currentStock = availableStock.get(materialId);
            availableStock.put(materialId, currentStock.subtract(consumedQuantity));
        }
    }

    /**
     * Generates production suggestions for all valid products.
     * Iterates through products sorted by price (highest first) and calculates
     * maximum producible units based on available stock.
     */
    private ProductionSuggestionResponseDTO generateProductionSuggestions(
            List<Product> products, 
            Map<Long, BigDecimal> availableStock) {
        
        List<ProductionItemSuggestionDTO> suggestions = new java.util.ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {
            if (!hasValidMaterials(product)) {
                continue;
            }

            int maxUnits = calculateMaxProducibleUnits(product, availableStock);
            maxUnits = normalizeLimitedProduction(maxUnits);

            if (maxUnits <= 0) {
                continue;
            }

            ProductionItemSuggestionDTO suggestion = generateProductionItem(product, availableStock, maxUnits);
            suggestions.add(suggestion);
            totalValue = totalValue.add(suggestion.getTotalValue());
        }
        
        return new ProductionSuggestionResponseDTO(suggestions, totalValue);
    }

    /**
     * Checks if product has valid materials configured.
     */
    private boolean hasValidMaterials(Product product) {
        return product.getMaterials() != null && !product.getMaterials().isEmpty();
    }

    /**
     * Normalizes production units: if no material imposed a limit,
     * defaults to 1 unit to avoid Integer.MAX_VALUE propagation.
     */
    private int normalizeLimitedProduction(int maxUnits) {
        return maxUnits == Integer.MAX_VALUE ? DEFAULT_MIN_PRODUCTION_UNITS : maxUnits;
    }
}
