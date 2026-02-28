package br.com.java.autoflex.service.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.java.autoflex.domain.Product;
import br.com.java.autoflex.domain.ProductMaterial;
import br.com.java.autoflex.dto.production.ProductionItemSuggestionDTO;
import br.com.java.autoflex.dto.production.ProductionSuggestionResponseDTO;

/**
 * Greedy production strategy that prioritizes products with higher prices.
 * This strategy sorts products by price in descending order and produces
 * as many units as possible for each product until materials are exhausted.
 */
@Component
public class GreedyProductionAlgorithm implements ProductionAlgorithm {

    private static final int DEFAULT_MIN_PRODUCTION_UNITS = 1;

    @Override
    public ProductionSuggestionResponseDTO generateSuggestions(
            List<Product> products,
            Map<Long, BigDecimal> availableStock) {
        
        // Sort products by price (highest first)
        products.sort(Comparator.comparing(Product::getPrice).reversed());
        
        return generateProductionSuggestions(products, availableStock);
    }

    /**
     * Generates production suggestions for all valid products.
     * Iterates through products sorted by price (highest first) and calculates
     * maximum producible units based on available stock.
     */
    private ProductionSuggestionResponseDTO generateProductionSuggestions(
            List<Product> products,
            Map<Long, BigDecimal> availableStock) {
        
        List<ProductionItemSuggestionDTO> suggestions = new ArrayList<>();
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
                totalValue,
                product.getMaterials().stream()
                    .map(pm -> new ProductionItemSuggestionDTO.MaterialUsage(
                            pm.getRawMaterial().getId(),
                            pm.getRawMaterial().getName(),
                            pm.getQuantityRequired().multiply(BigDecimal.valueOf(unitsToProduce))
                    ))
                    .toList()
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
