package br.com.java.autoflex.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductionServiceImpl implements ProductionService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    private Map<Long, BigDecimal> loadAvailableStock() {
        Map<Long, BigDecimal> availableStock = new HashMap<>();
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        for (RawMaterial rm : rawMaterials) {
            availableStock.put(rm.getId(), rm.getStockQuantity());
        }
        return availableStock;
    }

    private int calculateMaxProducibleUnits(Product product, Map<Long, BigDecimal> availableStock){
        int maxUnits = Integer.MAX_VALUE;
        for (ProductMaterial pm : product.getMaterials()) {

            Long materialId = pm.getRawMaterial().getId();
            BigDecimal requiredQuantity = pm.getQuantityRequired();

            BigDecimal stock = availableStock.getOrDefault(materialId, BigDecimal.ZERO);

            if (requiredQuantity.compareTo(BigDecimal.ZERO) == 0) {
                // If the product requires 0 of this material, this material
                // should not limit production — ignore it for limiting purposes.
                continue;
            }

            int possibleUnits = stock.divide(requiredQuantity, 0, RoundingMode.DOWN).intValue();
            maxUnits = Math.min(maxUnits, possibleUnits);
        }
        return maxUnits;
    }

    private ProductionItemSuggestionDTO generateProductionItemSuggestion(Product product, Map<Long, BigDecimal> availableStock, int unitsToProduce) {
        BigDecimal totalValue = product.getPrice().multiply(BigDecimal.valueOf(unitsToProduce));
        for (ProductMaterial pm : product.getMaterials()) {
            Long materialId = pm.getRawMaterial().getId();
            BigDecimal requiredQuantity = pm.getQuantityRequired().multiply(BigDecimal.valueOf(unitsToProduce));
            // Update virtual stock
            availableStock.put(materialId, availableStock.get(materialId).subtract(requiredQuantity));
        }
        return new ProductionItemSuggestionDTO(
                product.getId(),
                product.getName(),
                unitsToProduce,
                product.getPrice(),
                totalValue
        );
    }

    private ProductionSuggestionResponseDTO generateProductionSuggestions(List<Product> products, Map<Long, BigDecimal> availableStock) {
        List<ProductionItemSuggestionDTO> suggestions = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {

            if (product.getMaterials() == null || product.getMaterials().isEmpty()) {
                continue;
            }

            // 5. Calculate max producible units based on available stock
            int maxUnits = calculateMaxProducibleUnits(product, availableStock);
            // If no material imposed a limit (still Integer.MAX_VALUE),
            // produce a single unit by default (avoids Integer.MAX_VALUE propagation).
            if (maxUnits == Integer.MAX_VALUE) {
                maxUnits = 1;
            }

            if (maxUnits <= 0) {
                continue;
            }

            // 6. Update virtual stock and calculate total value
            ProductionItemSuggestionDTO suggestion = generateProductionItemSuggestion(product, availableStock, maxUnits);
            BigDecimal productTotal = suggestion.getTotalValue();
            totalValue = totalValue.add(productTotal);

            suggestions.add(suggestion);
        }
        
        return new ProductionSuggestionResponseDTO(suggestions, totalValue);
    }

    @Override
    public ProductionSuggestionResponseDTO generateProductionSuggestionResponseDTO() {
        // 1. Fetch products with materials (avoid N+1 problem)
        ArrayList<Product> products = (ArrayList<Product>) productRepository.findAllWithMaterials();

        // 2. Create virtual stock map for raw materials
        Map<Long, BigDecimal> availableStock = loadAvailableStock();

        // 3. Sort products by biggest price first
        products.sort(Comparator.comparing(Product::getPrice).reversed());

        // 4. Generate production suggestions
        return generateProductionSuggestions(products, availableStock);
    }
}
